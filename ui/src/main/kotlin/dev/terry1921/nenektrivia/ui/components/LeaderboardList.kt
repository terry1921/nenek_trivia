package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens

@Composable
fun LeaderboardList(players: List<PlayerScore>) {
    val listState = rememberLazyListState()
    val size = LocalSizeTokens.current
    val spacing = LocalSpacingTokens.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = size.paddingLarge,
                vertical = spacing.topAppBarHeight
            ),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
        contentPadding = PaddingValues(bottom = size.paddingExtraLarge)
    ) {
        items(players, key = { it.position }) { player ->
            LeaderboardRow(player = player)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LeaderboardListPreview() {
    LeaderboardList(
        players = listOf(
            PlayerScore(1, "", "Alice", 1500),
            PlayerScore(2, "", "Bob", 1200),
            PlayerScore(3, "", "Charlie", 900),
            PlayerScore(4, "", "David", 800),
            PlayerScore(5, "", "Eve", 700),
            PlayerScore(6, "", "Frank", 600),
            PlayerScore(7, "", "Grace", 500),
            PlayerScore(8, "", "Heidi", 400),
            PlayerScore(9, "", "Ivan", 300),
            PlayerScore(10, "", "Judy", 200)
        )
    )
}
