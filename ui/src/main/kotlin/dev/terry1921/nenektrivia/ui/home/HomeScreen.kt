package dev.terry1921.nenektrivia.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onPlayClick: () -> Unit) {
    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val color = LocalColorTokens.current
    val size = LocalSizeTokens.current
    val shape = LocalShapeTokens.current
    val start = color.primary
    val end = color.tertiary

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        start.copy(alpha = 0.15f),
                        end.copy(alpha = 0.15f)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.caminando_rio),
            contentDescription = stringResource(R.string.home_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .alpha(0.7f)
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing.topAppBarHeight),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = typography.headlineLarge,
                color = color.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(spacing.small))
            Text(
                text = stringResource(R.string.app_subtitle),
                style = typography.titleMedium,
                color = color.textTertiary,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = size.paddingExtraLarge)
                .padding(bottom = size.paddingExtraLarge),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onPlayClick,
                shape = RoundedCornerShape(shape.radiusXl),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(size.buttonHeight)
            ) {
                Text(stringResource(R.string.play_button_label), color = color.text)
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(
            onPlayClick = {}
        )
    }
}
