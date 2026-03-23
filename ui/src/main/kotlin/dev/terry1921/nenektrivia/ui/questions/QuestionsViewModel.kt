package dev.terry1921.nenektrivia.ui.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.honor.SyncHonorForGameResultUseCase
import dev.terry1921.nenektrivia.domain.preferences.GetUserSettingsUseCase
import dev.terry1921.nenektrivia.domain.questions.GetQuestionsUseCase
import dev.terry1921.nenektrivia.model.question.QuestionModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val syncHonorForGameResultUseCase: SyncHonorForGameResultUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState

    private var timerJob: Job? = null
    private var cachedQuestions: List<QuestionModel> = emptyList()
    private var gameQuestions: MutableList<QuestionModel> = mutableListOf()
    private var totalQuestionsInGame: Int = 0

    private val initialTimeSeconds = 10f
    private val timerTickMs = 100L
    private val roundSize = 200
    private val pointsPerCorrect = 10

    init {
        Timber.d("QuestionsViewModel initialized")
        viewModelScope.launch {
            getUserSettingsUseCase().collect { settings ->
                Timber.d("Question settings updated. haptics=%s", settings.isHapticsEnabled)
                _uiState.value = _uiState.value.copy(isHapticsEnabled = settings.isHapticsEnabled)
            }
        }
        viewModelScope.launch { startNewGame() }
    }

    fun selectOption(optionId: String) {
        val current = _uiState.value
        if (current.question == null || current.revealAnswer) {
            Timber.d(
                "Option selection ignored. hasQuestion=%s revealAnswer=%s",
                current.question != null,
                current.revealAnswer
            )
            return
        }

        val isCorrect = current.question.options.any { option ->
            option.id == optionId && option.isCorrect
        }

        stopTimer()

        if (!isCorrect) {
            Timber.i(
                "Incorrect answer on question %s/%s. points=%s",
                current.questionIndex,
                current.totalQuestions,
                current.points
            )
            _uiState.value = current.copy(
                selectedOptionId = optionId,
                revealAnswer = true,
                showGameOverDialog = true
            )
            syncHonorForCurrentPoints()
            return
        }

        Timber.i(
            "Correct answer on question %s/%s. newPoints=%s",
            current.questionIndex,
            current.totalQuestions,
            current.points + pointsPerCorrect
        )
        _uiState.value = current.copy(
            selectedOptionId = optionId,
            revealAnswer = true,
            points = current.points + pointsPerCorrect,
            showGameOverDialog = false
        )
    }

    fun nextQuestion() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current.showGameOverDialog) {
                Timber.d("Next question ignored because game over dialog is showing")
                return@launch
            }
            if (current.timeRemainingSeconds <= 0f) {
                Timber.d("Next question requested after timer finished, starting new game")
                startNewGame()
                return@launch
            }
            if (!current.revealAnswer) {
                Timber.d("Next question ignored because answer has not been revealed yet")
                return@launch
            }
            Timber.d("Advancing to next question")
            loadNextQuestionFromGame()
        }
    }

    fun startAgain() {
        Timber.d("Starting game again by user request")
        viewModelScope.launch { startNewGame() }
    }

    fun onExitGame() {
        Timber.i("Exiting game. finalPoints=%s", _uiState.value.points)
        stopTimer()
        gameQuestions.clear()
        totalQuestionsInGame = 0
        _uiState.value = QuestionUiState(
            points = 0,
            showWinnerDialog = false,
            showGameOverDialog = false,
            isHapticsEnabled = _uiState.value.isHapticsEnabled
        )
    }

    private suspend fun startNewGame() {
        Timber.d("Starting new game")
        stopTimer()

        if (cachedQuestions.isEmpty()) {
            Timber.d("Question cache empty, fetching questions")
            cachedQuestions = runCatching {
                getQuestionsUseCase(forceRefresh = false)
            }.onFailure { error ->
                Timber.e(error, "Failed to fetch questions for new game")
            }.getOrElse {
                emptyList()
            }
        }

        if (cachedQuestions.isEmpty()) {
            Timber.w("No questions available to start game")
            _uiState.value = QuestionUiState(
                questionIndex = 1,
                totalQuestions = 0,
                question = null,
                revealAnswer = true,
                timeRemainingSeconds = 0f,
                points = 0,
                isHapticsEnabled = _uiState.value.isHapticsEnabled,
                showWinnerDialog = false,
                showGameOverDialog = false
            )
            return
        }

        totalQuestionsInGame = minOf(roundSize, cachedQuestions.size)
        gameQuestions = cachedQuestions.shuffled().take(totalQuestionsInGame).toMutableList()
        Timber.i(
            "New game prepared with %s questions from %s cached questions",
            totalQuestionsInGame,
            cachedQuestions.size
        )

        _uiState.value = QuestionUiState(
            questionIndex = 0,
            totalQuestions = totalQuestionsInGame,
            points = 0,
            isHapticsEnabled = _uiState.value.isHapticsEnabled,
            question = null,
            selectedOptionId = null,
            revealAnswer = false,
            timeRemainingSeconds = initialTimeSeconds,
            tip = null,
            showWinnerDialog = false,
            showGameOverDialog = false
        )

        loadNextQuestionFromGame()
    }

    private fun loadNextQuestionFromGame() {
        if (gameQuestions.isEmpty()) {
            stopTimer()
            Timber.i("All questions answered. player won with %s points", _uiState.value.points)
            _uiState.value = _uiState.value.copy(
                revealAnswer = true,
                timeRemainingSeconds = 0f,
                showWinnerDialog = true,
                showGameOverDialog = false
            )
            syncHonorForCurrentPoints()
            return
        }

        val selected = gameQuestions.removeAt(0)
        val questionIndex = totalQuestionsInGame - gameQuestions.size
        Timber.d(
            "Loading question %s/%s with id=%s",
            questionIndex,
            totalQuestionsInGame,
            selected.id
        )
        val options = listOf(
            QuestionOption(
                id = "${selected.id}_bad_01",
                text = selected.answerBad01,
                isCorrect = false
            ),
            QuestionOption(
                id = "${selected.id}_bad_02",
                text = selected.answerBad02,
                isCorrect = false
            ),
            QuestionOption(
                id = "${selected.id}_bad_03",
                text = selected.answerBad03,
                isCorrect = false
            ),
            QuestionOption(
                id = "${selected.id}_good",
                text = selected.answerGood,
                isCorrect = true
            )
        ).shuffled()

        _uiState.value = _uiState.value.copy(
            questionIndex = questionIndex,
            totalQuestions = totalQuestionsInGame,
            question = QuestionItem(
                id = selected.id,
                category = selected.category,
                text = selected.question,
                options = options
            ),
            selectedOptionId = null,
            revealAnswer = false,
            timeRemainingSeconds = initialTimeSeconds,
            tip = selected.tip,
            showWinnerDialog = false,
            showGameOverDialog = false
        )

        startTimer()
    }

    private fun startTimer() {
        stopTimer()
        Timber.d("Starting question timer")
        timerJob = viewModelScope.launch {
            while (true) {
                delay(timerTickMs)
                val current = _uiState.value
                if (current.revealAnswer) break

                val nextTime = (current.timeRemainingSeconds - timerTickMs / 1000f)
                    .coerceAtLeast(0f)
                if (nextTime <= 0f) {
                    Timber.i(
                        "Question timer expired on question %s/%s. points=%s",
                        current.questionIndex,
                        current.totalQuestions,
                        current.points
                    )
                    _uiState.value = current.copy(
                        timeRemainingSeconds = 0f,
                        revealAnswer = true,
                        showGameOverDialog = true
                    )
                    syncHonorForCurrentPoints()
                    break
                }

                _uiState.value = current.copy(timeRemainingSeconds = nextTime)
            }
        }
    }

    private fun stopTimer() {
        if (timerJob != null) {
            Timber.d("Stopping question timer")
        }
        timerJob?.cancel()
        timerJob = null
    }

    private fun syncHonorForCurrentPoints() {
        viewModelScope.launch {
            runCatching {
                syncHonorForGameResultUseCase(_uiState.value.points)
            }.onSuccess {
                Timber.d("Honor synced for points=%s", _uiState.value.points)
            }.onFailure { error ->
                Timber.e(error, "Failed to sync honor for points=%s", _uiState.value.points)
            }
        }
    }

    override fun onCleared() {
        Timber.d("QuestionsViewModel cleared")
        stopTimer()
        super.onCleared()
    }
}
