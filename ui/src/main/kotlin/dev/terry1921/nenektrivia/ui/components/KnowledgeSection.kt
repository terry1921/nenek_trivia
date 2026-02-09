package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.model.category.Category
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.NenekPalette

@Composable
fun KnowledgeSection(knowledge: Map<Category, Int>) {
    val color = LocalColorTokens.current
    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val shape = LocalShapeTokens.current

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(shape.radiusLg))
            .background(color.surface.copy(alpha = 0.6f))
            .padding(spacing.large)
    ) {
        Text(
            text = stringResource(R.string.knowledge_bar),
            style = typography.titleMedium,
            color = color.onSurface
        )
        Spacer(Modifier.height(spacing.medium))

        // Mantener orden de categorías fijo
        val ordered = listOf(
            Category.Art,
            Category.Sports,
            Category.General,
            Category.Geography,
            Category.History
        )

        ordered.forEachIndexed { idx, cat ->
            val percent = knowledge[cat]?.coerceIn(1, 100) ?: 0
            KnowledgeBar(
                label = when (cat) {
                    Category.Art -> stringResource(R.string.knowledge_art)
                    Category.Sports -> stringResource(R.string.knowledge_sports)
                    Category.General -> stringResource(R.string.knowledge_general)
                    Category.Geography -> stringResource(R.string.knowledge_geography)
                    Category.History -> stringResource(R.string.knowledge_history)
                    else -> stringResource(R.string.knowledge_other) // No debería ocurrir
                },
                percent = percent,
                trackColor = color.surfaceVariant.copy(alpha = 0.7f),
                barColor = categoryColor(cat)
            )
            if (idx != ordered.lastIndex) Spacer(Modifier.height(spacing.medium))
        }
    }
}

private fun categoryColor(cat: Category): Color = when (cat) {
    Category.Art -> NenekPalette.Magenta
    Category.Sports -> NenekPalette.Orange
    Category.General -> NenekPalette.Pink
    Category.Geography -> NenekPalette.Red
    Category.History -> NenekPalette.DarkRed
    else -> NenekPalette.SurfaceGray // Fallback
}

@Preview(name = "Knowledge Section")
@Composable
private fun KnowledgeSectionPreview() {
    KnowledgeSection(
        knowledge = mapOf(
            Category.Art to 80,
            Category.Sports to 60,
            Category.General to 90,
            Category.Geography to 40,
            Category.History to 70
        )
    )
}
