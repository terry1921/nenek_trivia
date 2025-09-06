package dev.terry1921.nenektrivia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.terry1921.nenektrivia.ui.theme.NenekTheme
import dev.terry1921.nenektrivia.ui.tokens.TokenTheme

@Composable
fun NenekApp() {
    val dark = isSystemInDarkTheme()
    NenekTheme(darkTheme = dark) {
        // Ejemplo usando tokens directamente
        val s = TokenTheme.spacing
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .background(TokenTheme.colors.background)
                .padding(s.large)
        ) {
            Text(
                text = "Nenek Trivia",
                style = TokenTheme.typography.headlineLarge,
                color = TokenTheme.colors.onBackground
            )
        }
    }
}
