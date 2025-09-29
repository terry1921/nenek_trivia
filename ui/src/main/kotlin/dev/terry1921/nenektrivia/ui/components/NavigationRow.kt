package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens

@Composable
fun NavigationRow(title: String, currentValue: String, onClick: () -> Unit) {
    val color = LocalColorTokens.current
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(title) },
        supportingContent = {
            Text(
                currentValue,
                color = color.onSurfaceVariant
            )
        },
        trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = title) }
    )
}
