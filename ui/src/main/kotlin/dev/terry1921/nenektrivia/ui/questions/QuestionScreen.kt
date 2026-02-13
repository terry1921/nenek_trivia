package dev.terry1921.nenektrivia.ui.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.components.AnswerOptionCard
import dev.terry1921.nenektrivia.ui.components.AnswerOptionState
import dev.terry1921.nenektrivia.ui.components.KnowledgeTipCard
import dev.terry1921.nenektrivia.ui.components.QuestionHeader
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import java.util.Locale
import kotlin.math.max

@Composable
fun QuestionScreen(
    viewModel: QuestionsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    if (state.showWinnerDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = { Text(text = stringResource(R.string.winner_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onExitGame()
                        onBack()
                    }
                ) {
                    Text(text = stringResource(R.string.winner_dialog_accept))
                }
            }
        )
    }

    if (showExitDialog && !state.showWinnerDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = stringResource(R.string.exit_quiz_title)) },
            text = { Text(text = stringResource(R.string.exit_quiz_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        viewModel.onExitGame()
                        onBack()
                    }
                ) {
                    Text(text = stringResource(R.string.exit_quiz_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(text = stringResource(R.string.exit_quiz_cancel))
                }
            }
        )
    }

    QuestionScreenContent(
        state = state,
        onOptionSelected = viewModel::selectOption,
        onNextQuestion = viewModel::nextQuestion,
        onBackRequest = { showExitDialog = true },
        modifier = modifier
    )
}

@Composable
private fun QuestionScreenContent(
    state: QuestionUiState,
    onOptionSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onBackRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = LocalColorTokens.current
    val spacing = LocalSpacingTokens.current
    val size = LocalSizeTokens.current
    val typography = LocalTypographyTokens.current
    val question = state.question
    val timeText = String.format(
        Locale.getDefault(),
        "%.1fs",
        max(0f, state.timeRemainingSeconds)
    )
    val isTimedOut = state.timeRemainingSeconds <= 0f

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.flores_pradera),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.7f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color.background.copy(alpha = 0.75f),
                            color.surface.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = size.paddingLarge)
                .padding(top = spacing.topAppBarHeight, bottom = size.paddingExtraLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {
            if (question == null) {
                Text(
                    text = stringResource(R.string.cargando_pregunta),
                    style = typography.bodyMedium,
                    color = color.textSecondary
                )
            } else {
                LoadedQuestionContent(
                    state = state,
                    question = question,
                    timeText = timeText,
                    isTimedOut = isTimedOut,
                    onOptionSelected = onOptionSelected,
                    onNextQuestion = onNextQuestion,
                    onBackRequest = onBackRequest
                )
            }
        }
    }
}

@Composable
private fun LoadedQuestionContent(
    state: QuestionUiState,
    question: QuestionItem,
    timeText: String,
    isTimedOut: Boolean,
    onOptionSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onBackRequest: () -> Unit
) {
    val spacing = LocalSpacingTokens.current
    val color = LocalColorTokens.current
    val typography = LocalTypographyTokens.current

    QuestionHeader(
        questionIndex = state.questionIndex,
        totalQuestions = state.totalQuestions,
        category = question.category,
        points = state.points,
        modifier = Modifier.fillMaxWidth(),
        onBackClick = onBackRequest
    )

    Spacer(Modifier.height(spacing.medium))

    Text(
        text = question.text,
        style = typography.headlineSmall,
        color = color.onBackground
    )

    Spacer(Modifier.height(spacing.medium))

    QuestionOptions(
        options = question.options,
        selectedOptionId = state.selectedOptionId,
        revealAnswer = state.revealAnswer,
        onOptionSelected = onOptionSelected
    )

    state.tip?.let { tip ->
        Spacer(Modifier.height(spacing.small))
        KnowledgeTipCard(text = tip, modifier = Modifier.fillMaxWidth())
    }

    Spacer(Modifier.height(spacing.small))

    QuestionFooter(
        timeText = timeText,
        isTimedOut = isTimedOut,
        isAnswerRevealed = state.revealAnswer,
        onNextQuestion = onNextQuestion,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun QuestionOptions(
    options: List<QuestionOption>,
    selectedOptionId: String?,
    revealAnswer: Boolean,
    onOptionSelected: (String) -> Unit
) {
    val spacing = LocalSpacingTokens.current
    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        options.forEachIndexed { index, option ->
            AnswerOptionCard(
                label = ('A' + index).toString(),
                text = option.text,
                state = resolveOptionState(
                    revealAnswer = revealAnswer,
                    option = option,
                    selectedOptionId = selectedOptionId
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !revealAnswer,
                onClick = { onOptionSelected(option.id) }
            )
        }
    }
}

@Composable
private fun QuestionFooter(
    timeText: String,
    isTimedOut: Boolean,
    isAnswerRevealed: Boolean,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacingTokens.current
    val color = LocalColorTokens.current
    val size = LocalSizeTokens.current
    val typography = LocalTypographyTokens.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = color.textSecondary,
                modifier = Modifier.size(size.iconButtonSize)
            )
            Spacer(Modifier.width(spacing.extraSmall))
            Column {
                Text(
                    text = stringResource(R.string.question_time_label),
                    style = typography.labelSmall,
                    color = color.textTertiary
                )
                Text(
                    text = timeText,
                    style = typography.labelLarge,
                    color = color.textSecondary
                )
            }
        }
        Button(
            onClick = onNextQuestion,
            enabled = isAnswerRevealed,
            modifier = Modifier.height(size.buttonHeight)
        ) {
            val labelRes = if (isTimedOut) {
                R.string.question_new_game_label
            } else {
                R.string.question_next_label
            }
            Text(text = stringResource(labelRes))
        }
    }
}

private fun resolveOptionState(
    revealAnswer: Boolean,
    option: QuestionOption,
    selectedOptionId: String?
): AnswerOptionState = when {
    revealAnswer && option.isCorrect -> AnswerOptionState.Correct
    revealAnswer && option.id == selectedOptionId -> AnswerOptionState.Incorrect
    else -> AnswerOptionState.Default
}

@Preview()
@Composable
fun QuestionScreenPreview() {
    Surface {
        QuestionScreenContent(
            state = QuestionUiState(
                questionIndex = 8,
                totalQuestions = 10,
                points = 2480,
                question = QuestionItem(
                    id = "-L0MlW6DcfrqNKAU2gKX",
                    category = "Geografia Local",
                    text = "¿Dónde se encuentra Chapulhuacanito?",
                    options = listOf(
                        QuestionOption(id = "San Martin", text = "San Martin", isCorrect = false),
                        QuestionOption(id = "Axtla", text = "Axtla", isCorrect = false),
                        QuestionOption(id = "Xilitla", text = "Xilitla", isCorrect = false),
                        QuestionOption(
                            id = "Tamazunchale",
                            text = "Tamazunchale",
                            isCorrect = true
                        )
                    )
                ),
                selectedOptionId = "Tamazunchale",
                revealAnswer = true,
                timeRemainingSeconds = 8.2f,
                tip = """
                    Chapulhuacanito es una importante delegación perteneciente
                    al municipio de Tamazunchale, en la región de la Huasteca Potosina.
                """.trimIndent()
            ),
            onOptionSelected = {},
            onNextQuestion = {},
            onBackRequest = {}
        )
    }
}
