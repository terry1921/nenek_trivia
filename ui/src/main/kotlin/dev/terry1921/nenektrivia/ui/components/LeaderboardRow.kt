package dev.terry1921.nenektrivia.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun LeaderboardRow(position: Int, player: PlayerScore) {
    val size = LocalSizeTokens.current
    val spacing = LocalSpacingTokens.current
    val shape = LocalShapeTokens.current
    val color = LocalColorTokens.current
    val typography = LocalTypographyTokens.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(shape.radiusLg),
        colors = CardDefaults.cardColors(
            containerColor = color.surface.copy(alpha = 0.78f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = size.elevationSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = size.paddingMedium,
                    vertical = size.paddingSmall
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.position, position),
                style = typography.titleMedium,
                color = color.primary,
                modifier = Modifier.width(spacing.extraExtraExtraLarge)
            )

            Avatar(
                url = player.image,
                size = size.avatarSmall,
                contentDescription = player.username
            )

            Spacer(Modifier.width(spacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.username,
                    style = typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = stringResource(R.string.points, player.points),
                style = typography.bodyMedium,
                color = color.onSurfaceVariant
            )
        }
    }
}

@Preview(
    name = "Leaderboard Row"
)
@Composable
fun LeaderboardRowPreview() {
    PreviewContent(
        backgroundColor = Color.Transparent,
        paddingValues = PaddingValues(4.dp)
    ) {
        LeaderboardRow(
            position = 1,
            player = PlayerScore(
                id = "test-user",
                username = "Usuario de prueba",
                image = "https://i.pravatar.cc/150?u=terry@",
                points = 1234
            )
        )
    }
}
