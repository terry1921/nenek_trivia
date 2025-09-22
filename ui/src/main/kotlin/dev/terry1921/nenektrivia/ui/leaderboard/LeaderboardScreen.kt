package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.components.ErrorContent
import dev.terry1921.nenektrivia.ui.components.LeaderboardList
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens

@Composable
fun LeaderboardRoute(viewModel: LeaderboardViewModel, onBack: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsState()
    LeaderboardScreen(
        state = state,
        onRetry = viewModel::retry
    )
}

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    state: LeaderboardUiState,
    onRetry: () -> Unit = {}
) {
    val color = LocalColorTokens.current

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.sentada_cafetales),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            alpha = 0.7f
        )

        Box(
            Modifier
                .matchParentSize()
                .background(color.surface.copy(alpha = 0.08f))
        )

        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> ErrorContent(message = state.error, onRetry = onRetry)

            else -> LeaderboardList(players = state.players)
        }
    }
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen(
        state = LeaderboardUiState(
            isLoading = false,
            players = listOf(
                PlayerScore(1, null, "Terry1921", 1500),
                PlayerScore(2, null, "JaneDoe", 1200),
                PlayerScore(3, null, "JohnSmith", 1100),
                PlayerScore(4, null, "AliceWonder", 1000),
                PlayerScore(5, null, "BobBuilder", 900),
                PlayerScore(6, null, "CharlieBrown", 800),
                PlayerScore(7, null, "DoraExplorer", 700),
                PlayerScore(8, null, "EveOnline", 600),
                PlayerScore(9, null, "FrankCastle", 500),
                PlayerScore(10, null, "GraceHopper", 400)
            )
        )
    )
}
