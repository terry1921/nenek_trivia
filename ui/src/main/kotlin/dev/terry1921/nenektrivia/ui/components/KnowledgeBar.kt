package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun KnowledgeBar(label: String, percent: Int, barColor: Color, trackColor: Color) {
    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val size = LocalSizeTokens.current
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = typography.bodyMedium)
            Text(text = "$percent%", style = typography.labelMedium)
        }
        Spacer(Modifier.height(spacing.small))
        LinearProgressIndicator(
            progress = percent / 100f,
            color = barColor,
            trackColor = trackColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(size.progressHeight)
                .clip(RoundedCornerShape(999.dp))
        )
    }
}

@Preview
@Composable
fun KnowledgeBarPreview() {
    PreviewContent(
        paddingValues = PaddingValues(8.dp)
    ) {
        KnowledgeBar(
            label = "Science",
            percent = 75,
            barColor = Color(0xFF4CAF50),
            trackColor = Color(0xFFE0E0E0)
        )
    }
}
