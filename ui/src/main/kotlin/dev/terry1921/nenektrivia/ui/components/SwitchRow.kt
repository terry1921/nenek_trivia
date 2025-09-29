package dev.terry1921.nenektrivia.ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens

@Composable
fun SwitchRow(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null
) {
    val color = LocalColorTokens.current
    ListItem(
        headlineContent = {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = subtitle?.let {
            { Text(it, color = color.onSurfaceVariant) }
        },
        trailingContent = {
            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        }
    )
}
