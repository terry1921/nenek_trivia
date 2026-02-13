package dev.terry1921.nenektrivia.ui.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.questions.GetQuestionsUseCase
import dev.terry1921.nenektrivia.model.question.QuestionModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val getQuestionsUseCase: GetQuestionsUseCase) :
    ViewModel() {
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
        viewModelScope.launch { startNewGame() }
    }

    fun selectOption(optionId: String) {
        val current = _uiState.value
        if (current.question == null || current.revealAnswer) return

        val isCorrect = current.question.options.any { option ->
            option.id == optionId && option.isCorrect
        }

        _uiState.value = current.copy(
            selectedOptionId = optionId,
            revealAnswer = true,
            points = current.points + if (isCorrect) pointsPerCorrect else 0
        )
        stopTimer()
    }

    fun nextQuestion() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current.timeRemainingSeconds <= 0f) {
                startNewGame()
                return@launch
            }
            if (!current.revealAnswer) return@launch
            loadNextQuestionFromGame()
        }
    }

    fun onExitGame() {
        stopTimer()
        gameQuestions.clear()
        totalQuestionsInGame = 0
        _uiState.value = QuestionUiState(points = 0, showWinnerDialog = false)
    }

    private suspend fun startNewGame() {
        stopTimer()

        if (cachedQuestions.isEmpty()) {
            cachedQuestions = runCatching {
                getQuestionsUseCase(forceRefresh = false)
            }.getOrElse {
                emptyList()
            }
        }

        if (cachedQuestions.isEmpty()) {
            _uiState.value = QuestionUiState(
                questionIndex = 1,
                totalQuestions = 0,
                question = null,
                revealAnswer = true,
                timeRemainingSeconds = 0f,
                points = 0,
                showWinnerDialog = false
            )
            return
        }

        totalQuestionsInGame = minOf(roundSize, cachedQuestions.size)
        gameQuestions = cachedQuestions.shuffled().take(totalQuestionsInGame).toMutableList()

        _uiState.value = QuestionUiState(
            questionIndex = 0,
            totalQuestions = totalQuestionsInGame,
            points = 0,
            question = null,
            selectedOptionId = null,
            revealAnswer = false,
            timeRemainingSeconds = initialTimeSeconds,
            tip = null,
            showWinnerDialog = false
        )

        loadNextQuestionFromGame()
    }

    private fun loadNextQuestionFromGame() {
        if (gameQuestions.isEmpty()) {
            stopTimer()
            _uiState.value = _uiState.value.copy(
                revealAnswer = true,
                timeRemainingSeconds = 0f,
                showWinnerDialog = true
            )
            return
        }

        val selected = gameQuestions.removeAt(0)
        val questionIndex = totalQuestionsInGame - gameQuestions.size
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
            showWinnerDialog = false
        )

        startTimer()
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(timerTickMs)
                val current = _uiState.value
                if (current.revealAnswer) break

                val nextTime = (current.timeRemainingSeconds - timerTickMs / 1000f)
                    .coerceAtLeast(0f)
                if (nextTime <= 0f) {
                    _uiState.value = current.copy(
                        timeRemainingSeconds = 0f,
                        revealAnswer = true
                    )
                    break
                }

                _uiState.value = current.copy(timeRemainingSeconds = nextTime)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }
}
