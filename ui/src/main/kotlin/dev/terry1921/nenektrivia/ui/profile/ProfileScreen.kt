package dev.terry1921.nenektrivia.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.terry1921.nenektrivia.model.category.Category
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.components.Avatar
import dev.terry1921.nenektrivia.ui.components.ErrorContent
import dev.terry1921.nenektrivia.ui.components.KnowledgeSection
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens

@Composable
fun ProfileScreen(state: ProfileUiState, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    val size = LocalSizeTokens.current
    val color = LocalColorTokens.current
    val spacing = LocalSpacingTokens.current

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.luciernagas_noche),
            contentDescription = null,
            modifier = Modifier
                .alpha(0.7f)
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .matchParentSize()
                .background(color.surface.copy(alpha = 0.08f))
        )

        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> ErrorContent(
                message = state.error,
                onRetry = onRetry,
                modifier = Modifier
            )

            else -> ProfileContent(
                state = state,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        vertical = spacing.topAppBarHeight,
                        horizontal = size.paddingExtraLarge
                    )
            )
        }
    }
}

@Composable
private fun ProfileContent(state: ProfileUiState, modifier: Modifier = Modifier) {
    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val color = LocalColorTokens.current
    val size = LocalSizeTokens.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(
            url = state.avatarUrl,
            size = size.avatarExtraExtraLarge,
            contentDescription = stringResource(R.string.profile_photo)
        )

        Spacer(Modifier.height(spacing.medium))

        Text(
            text = state.displayName ?: state.email.orEmpty(),
            style = typography.titleLarge,
            color = color.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(spacing.extraExtraLarge))

        KnowledgeSection(knowledge = state.knowledge)
        Spacer(Modifier.height(spacing.large))
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val fakeKnowledge = mapOf(
        Category.Art to 80,
        Category.Sports to 45,
        Category.General to 70,
        Category.Geography to 30,
        Category.History to 55
    )
    ProfileScreen(
        state = ProfileUiState(
            isLoading = false,
            displayName = "Enrique Espinoza",
            email = "terry@nenek.com",
            avatarUrl = "https://i.pravatar.cc/300",
            knowledge = fakeKnowledge,
            error = null
        ),
        onRetry = {}
    )
}
