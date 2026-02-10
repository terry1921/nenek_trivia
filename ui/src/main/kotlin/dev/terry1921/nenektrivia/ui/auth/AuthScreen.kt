package dev.terry1921.nenektrivia.ui.auth

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.components.SocialButtonVariant
import dev.terry1921.nenektrivia.ui.components.SocialLoginButton
import dev.terry1921.nenektrivia.ui.theme.NenekTheme
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.asMaterialShapes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateMain: () -> Unit,
    onNavigatePrivacyPolicy: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val clientId = stringResource(R.string.default_web_client_id)
    val googleSignInClient = remember {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context, gso)
    }
    val googleSignInLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                viewModel.onGoogleCancelled()
                return@rememberLauncherForActivityResult
            }
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken.isNullOrBlank()) {
                    viewModel.onGoogleError("No se pudo obtener el token de Google.")
                } else {
                    viewModel.onGoogleToken(idToken)
                }
            } catch (error: ApiException) {
                viewModel.onGoogleError()
            }
        }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AuthEffect.NavigateToMain -> onNavigateMain()

                AuthEffect.NavigateToPrivacyPolicy -> onNavigatePrivacyPolicy()

                AuthEffect.LaunchGoogleSignIn -> {
                    googleSignInClient.signOut()
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                }
            }
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    AuthScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onGoogleClick = viewModel::onGoogleClick,
        onFacebookClick = viewModel::onFacebookClick,
        onPrivacyClick = viewModel::onPrivacyPolicyClick
    )
}

@Composable
private fun AuthScreenContent(
    state: AuthUiState,
    snackbarHostState: SnackbarHostState,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val size = LocalSizeTokens.current
    val shapes = LocalShapeTokens.current
    val color = LocalColorTokens.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = color.surface
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg_nenek_minimal),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = size.paddingMedium,
                        vertical = size.paddingLarge
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(size.iconMaxHeight)
                        .shadow(
                            elevation = size.elevationLarge,
                            shape = RoundedCornerShape(shapes.radiusSm),
                            clip = true
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(size.iconMax)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(Modifier.height(spacing.extraExtraLarge))
                Text(
                    text = stringResource(R.string.welcome_app),
                    style = typography.displayMedium,
                    color = color.darkText,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(spacing.small))
                Text(
                    text = stringResource(R.string.description_app),
                    style = typography.bodyMedium,
                    color = color.darkText.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(spacing.large))
                SocialLoginButton(
                    label = stringResource(R.string.google_login),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_google),
                    enabled = !state.isGoogleLoading,
                    loading = state.isGoogleLoading,
                    onClick = onGoogleClick,
                    variant = SocialButtonVariant.Filled,
                    shape = shapes.asMaterialShapes().medium
                )
                Spacer(Modifier.height(spacing.medium))
                SocialLoginButton(
                    label = stringResource(R.string.facebook_login),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_facebook),
                    enabled = !state.isFacebookLoading,
                    loading = state.isFacebookLoading,
                    onClick = onFacebookClick,
                    variant = SocialButtonVariant.Outlined,
                    shape = shapes.asMaterialShapes().medium
                )
                Spacer(Modifier.height(spacing.medium))
                Text(
                    text = stringResource(R.string.privacy),
                    style = typography.bodySmall,
                    color = color.link,
                    modifier = Modifier
                        .clickable(onClick = onPrivacyClick)
                        .padding(size.paddingSmall)
                        .align(Alignment.End)
                )
                Spacer(Modifier.height(spacing.large))
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    }
}

@Preview(
    name = "Auth Screen Light",
    showSystemUi = true,
    showBackground = true,
    locale = "es"
)
@Composable
fun AuthScreenPreview() {
    NenekTheme {
        AuthScreenContent(
            state = AuthUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleClick = {},
            onFacebookClick = {},
            onPrivacyClick = {}
        )
    }
}

@Preview(
    name = "Auth Screen Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "es"
)
@Composable
fun AuthScreenDarkPreview() {
    NenekTheme {
        AuthScreenContent(
            state = AuthUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleClick = {},
            onFacebookClick = {},
            onPrivacyClick = {}
        )
    }
}
