package dev.terry1921.nenektrivia.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import java.text.NumberFormat

enum class AnswerOptionState {
    Default,
    Selected,
    Correct,
    Incorrect
}

@Composable
fun QuestionHeader(
    questionIndex: Int,
    totalQuestions: Int,
    category: String,
    points: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val spacing = LocalSpacingTokens.current
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.extraSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = color.textSecondary
                )
            }
            Text(
                text = "PREGUNTA $questionIndex DE $totalQuestions",
                style = typography.labelSmall,
                color = color.textTertiary
            )
            QuestionScoreChip(points = points)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = color.textSecondary,
                modifier = Modifier.size(LocalSizeTokens.current.iconButtonSize)
            )
            Spacer(Modifier.width(spacing.extraSmall))
            Text(
                text = category,
                style = typography.bodySmall,
                color = color.textSecondary
            )
        }
    }
}

@Composable
fun QuestionScoreChip(points: Int, modifier: Modifier = Modifier) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val shape = LocalShapeTokens.current
    val spacing = LocalSpacingTokens.current
    val formattedPoints = remember(points) { NumberFormat.getNumberInstance().format(points) }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(shape.radiusXl),
        color = color.primaryContainer.copy(alpha = 0.35f),
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = spacing.large,
                vertical = spacing.small
            )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color.primary
            )
            Spacer(Modifier.width(spacing.extraSmall))
            Text(
                text = formattedPoints,
                style = typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = color.primary
            )
        }
    }
}

@Composable
fun AnswerOptionCard(
    label: String,
    text: String,
    state: AnswerOptionState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val shape = LocalShapeTokens.current
    val size = LocalSizeTokens.current
    val spacing = LocalSpacingTokens.current
    val correctColor = Color(0xFF4CAF50)

    val backgroundColor = when (state) {
        AnswerOptionState.Correct -> correctColor.copy(alpha = 0.2f)
        AnswerOptionState.Incorrect -> color.errorContainer.copy(alpha = 0.5f)
        AnswerOptionState.Selected -> color.primaryContainer.copy(alpha = 0.4f)
        AnswerOptionState.Default -> color.surface.copy(alpha = 0.08f)
    }
    val borderColor = when (state) {
        AnswerOptionState.Correct -> correctColor
        AnswerOptionState.Incorrect -> color.error
        AnswerOptionState.Selected -> color.primary
        AnswerOptionState.Default -> color.outline.copy(alpha = 0.5f)
    }
    val textColor = when (state) {
        AnswerOptionState.Correct -> correctColor
        AnswerOptionState.Incorrect -> color.onError
        AnswerOptionState.Selected -> color.onPrimaryContainer
        AnswerOptionState.Default -> color.textSecondary
    }

    Surface(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(shape.radiusLg),
        color = backgroundColor,
        border = BorderStroke(size.strokeWidthSmall, borderColor),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = spacing.large,
                vertical = spacing.medium
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OptionIndicator(label = label, state = state)
            Spacer(Modifier.width(spacing.large))
            Text(
                text = text,
                style = typography.titleSmall,
                color = textColor
            )
        }
    }
}

@Composable
fun KnowledgeTipCard(text: String, modifier: Modifier = Modifier) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val spacing = LocalSpacingTokens.current
    val shape = LocalShapeTokens.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(shape.radiusLg),
        color = color.link.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.link.copy(alpha = 0.4f)),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = color.link
                )
                Spacer(Modifier.width(spacing.extraSmall))
                Text(
                    text = stringResource(R.string.question_tip_label),
                    style = typography.labelSmall,
                    color = color.link
                )
            }
            Text(
                text = text,
                style = typography.bodyMedium,
                color = color.textSecondary
            )
        }
    }
}

@Composable
private fun OptionIndicator(label: String, state: AnswerOptionState) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    val size = LocalSizeTokens.current
    val correctColor = Color(0xFF4CAF50)

    val indicatorBackground = when (state) {
        AnswerOptionState.Correct -> correctColor
        AnswerOptionState.Incorrect -> color.error
        AnswerOptionState.Selected -> color.primaryContainer.copy(alpha = 0.6f)
        AnswerOptionState.Default -> color.surface.copy(alpha = 0.1f)
    }
    val indicatorBorder = when (state) {
        AnswerOptionState.Correct -> correctColor
        AnswerOptionState.Incorrect -> color.error
        AnswerOptionState.Selected -> color.primary
        AnswerOptionState.Default -> color.outline.copy(alpha = 0.6f)
    }
    val indicatorContentColor = when (state) {
        AnswerOptionState.Correct -> color.onPrimary
        AnswerOptionState.Incorrect -> color.onError
        AnswerOptionState.Selected -> color.onPrimaryContainer
        AnswerOptionState.Default -> color.textSecondary
    }

    Box(
        modifier = Modifier
            .size(size.avatarSmall)
            .clip(CircleShape)
            .background(indicatorBackground)
            .border(BorderStroke(size.strokeWidthSmall, indicatorBorder), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            AnswerOptionState.Correct -> Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = indicatorContentColor
            )

            AnswerOptionState.Incorrect -> Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = indicatorContentColor
            )

            else -> Text(
                text = label,
                style = typography.labelLarge,
                color = indicatorContentColor
            )
        }
    }
}

@Preview(
    name = "Question Header Light"
)
@Composable
fun QuestionHeaderPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        QuestionHeader(
            modifier = Modifier.padding(8.dp),
            questionIndex = 8,
            totalQuestions = 10,
            category = "Geografia Local",
            points = 1000,
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Question Header Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun QuestionHeaderDarkPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        QuestionHeader(
            modifier = Modifier.padding(8.dp),
            questionIndex = 8,
            totalQuestions = 10,
            category = "Geografia Local",
            points = 2480,
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Answer Option Card Light"
)
@Composable
fun AnswerOptionCardPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnswerOptionCard(
            modifier = Modifier.padding(8.dp),
            label = "A",
            text = "Respuesta",
            state = AnswerOptionState.Default,
            onClick = {}
        )
    }
}

@Preview(
    name = "Answer Option Card Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AnswerOptionCardDarkPreview() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnswerOptionCard(
            modifier = Modifier.padding(8.dp),
            label = "A",
            text = "Respuesta",
            state = AnswerOptionState.Default,
            onClick = {}
        )
    }
}
