package dev.terry1921.nenektrivia.ui.options

import androidx.compose.runtime.Composable
import dev.terry1921.nenektrivia.ui.components.CenteredLabel

@Composable
fun OptionsScreen(onLogoutClick: () -> Unit) =
    CenteredLabel("Opciones (logout pendiente de Firebase)")
