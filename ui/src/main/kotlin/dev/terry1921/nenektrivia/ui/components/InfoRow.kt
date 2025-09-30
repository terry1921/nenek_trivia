package dev.terry1921.nenektrivia.ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens

@Composable
fun InfoRow(title: String, value: String) {
    val color = LocalColorTokens.current
    ListItem(
        headlineContent = { Text(title) },
        trailingContent = { Text(value, color = color.onSurfaceVariant) }
    )
}

@Preview
@Composable
fun InfoRowPreview() {
    Surface {
        InfoRow(title = "Versión", value = "1.0.0")
    }
}
