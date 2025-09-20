package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun CenteredLabel(text: String) {
    val typography = LocalTypographyTokens.current
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}
