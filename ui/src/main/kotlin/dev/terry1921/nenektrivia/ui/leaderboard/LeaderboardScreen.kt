package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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
                PlayerScore(id = "1", image = null, username = "Terry1921", points = 1500),
                PlayerScore(id = "2", image = null, username = "JaneDoe", points = 1200),
                PlayerScore(id = "3", image = null, username = "JohnSmith", points = 1100),
                PlayerScore(id = "4", image = null, username = "AliceWonder", points = 1000),
                PlayerScore(id = "5", image = null, username = "BobBuilder", points = 900),
                PlayerScore(id = "6", image = null, username = "CharlieBrown", points = 800),
                PlayerScore(id = "7", image = null, username = "DoraExplorer", points = 700),
                PlayerScore(id = "8", image = null, username = "EveOnline", points = 600),
                PlayerScore(id = "9", image = null, username = "FrankCastle", points = 500),
                PlayerScore(id = "10", image = null, username = "GraceHopper", points = 400)
            )
        )
    )
}
