package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                start = size.paddingLarge,
                end = size.paddingLarge,
                top = spacing.topAppBarHeight,
                bottom = 0.dp
            ),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(spacing.medium),
        contentPadding = PaddingValues(bottom = size.paddingExtraLarge)
    ) {
        itemsIndexed(
            items = players,
            key = { index, player ->
                player.id.ifBlank { "$index-${player.username}" }
            }
        ) { index, player ->
            LeaderboardRow(position = index + 1, player = player)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LeaderboardListPreview() {
    LeaderboardList(
        players = listOf(
            PlayerScore(id = "1", image = "", username = "Alice", points = 1500),
            PlayerScore(id = "2", image = "", username = "Bob", points = 1200),
            PlayerScore(id = "3", image = "", username = "Charlie", points = 900),
            PlayerScore(id = "4", image = "", username = "David", points = 800),
            PlayerScore(id = "5", image = "", username = "Eve", points = 700),
            PlayerScore(id = "6", image = "", username = "Frank", points = 600),
            PlayerScore(id = "7", image = "", username = "Grace", points = 500),
            PlayerScore(id = "8", image = "", username = "Heidi", points = 400),
            PlayerScore(id = "9", image = "", username = "Ivan", points = 300),
            PlayerScore(id = "10", image = "", username = "Judy", points = 200)
        )
    )
}
