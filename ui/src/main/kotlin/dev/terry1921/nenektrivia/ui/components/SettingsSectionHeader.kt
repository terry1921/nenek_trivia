package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun SettingsSectionHeader(title: String) {
    val typography = LocalTypographyTokens.current
    val color = LocalColorTokens.current
    Text(
        text = title,
        style = typography.labelLarge,
        color = color.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
    HorizontalDivider(Modifier.padding(bottom = 4.dp))
}
