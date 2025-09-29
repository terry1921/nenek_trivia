package dev.terry1921.nenektrivia.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ProfileRoute(viewModel: ProfileViewModel, onBack: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsState()
    ProfileScreen(
        state = state,
        onRetry = viewModel::retry
    )
}
