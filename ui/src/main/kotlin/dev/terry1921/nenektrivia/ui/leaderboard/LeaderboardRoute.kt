package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LeaderboardRoute(viewModel: LeaderboardViewModel) {
    val state by viewModel.uiState.collectAsState()
    LeaderboardScreen(
        state = state,
        onRetry = viewModel::retry
    )
}
