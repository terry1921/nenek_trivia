package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun SettingsSectionHeader(title: String) {
    val size = LocalSizeTokens.current
    Text(
        text = title,
        style = LocalTypographyTokens.current.labelLarge,
        color = LocalColorTokens.current.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = size.paddingLarge,
                vertical = size.paddingSmall
            )
    )
    HorizontalDivider(Modifier.padding(bottom = size.paddingExtraSmall))
}
