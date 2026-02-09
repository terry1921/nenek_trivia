package dev.terry1921.nenektrivia.ui.auth

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.terry1921.nenektrivia.model.auth.AuthService
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.asMaterialShapes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateMain: () -> Unit,
    onNavigatePrivacyPolicy: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            if (effect is AuthEffect.NavigateToMain) onNavigateMain()
            if (effect is AuthEffect.NavigateToPrivacyPolicy) onNavigatePrivacyPolicy()
        }
    }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    val typography = LocalTypographyTokens.current
    val spacing = LocalSpacingTokens.current
    val shapes = LocalShapeTokens.current
    val color = LocalColorTokens.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = color.surface
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg_nenek_minimal), // <-- agrega tu imagen
                contentDescription = null,
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 48.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )

            // Contenido inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = spacing.medium, vertical = spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(150.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp), clip = true)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .size(width = 150.dp, height = 150.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(Modifier.height(spacing.extraExtraLarge))
                Text(
                    text = "Bienvenido a\nNenek Trivia",
                    style = typography.displayMedium,
                    color = color.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(spacing.small))
                Text(
                    text = "Participa en cuestionarios rápidos y sube en la clasificación.\n" +
                        "Todas tus curiosidades en un solo lugar.",
                    style = typography.bodyMedium,
                    color = color.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(spacing.large))

                // Botón social primario (estilo filled / morado en tu tema)
                SocialLoginButton(
                    label = "Inicia sesión con Google",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_google),
                    enabled = !state.isGoogleLoading,
                    loading = state.isGoogleLoading,
                    onClick = {
                        // TODO: Obtain real ID token from Google Sign-In SDK
                        viewModel.onGoogleSignIn("PENDING_REAL_TOKEN")
                    },
                    variant = SocialButtonVariant.Filled,
                    shape = shapes.asMaterialShapes().medium
                )
                Spacer(Modifier.height(spacing.medium))

                // Botón social secundario (outlined / fondo claro)
                SocialLoginButton(
                    label = "Inicia sesión con Facebook",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_facebook),
                    enabled = !state.isFacebookLoading,
                    loading = state.isFacebookLoading,
                    onClick = {
                        // TODO: Obtain real access token from Facebook Login SDK
                        viewModel.onFacebookSignIn("PENDING_REAL_TOKEN")
                    },
                    variant = SocialButtonVariant.Outlined,
                    shape = shapes.asMaterialShapes().medium
                )
                Spacer(Modifier.height(spacing.medium))
                // enlace a politica de privacidad
                Text(
                    text = "Política de privacidad",
                    style = typography.bodySmall,
                    color = color.link,
                    modifier = Modifier
                        .clickable { viewModel.onPrivacyPolicyClick() }
                        .padding(spacing.small)
                        .align(Alignment.End)
                )
                Spacer(Modifier.height(spacing.large))
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    val dummyAuthService = object : AuthService {
        override suspend fun signInWithGoogle(idToken: String): Result<Unit> = Result.success(Unit)
        override suspend fun signInWithFacebook(accessToken: String): Result<Unit> = Result.success(Unit)
        override fun signOut() {}
        override val isUserLoggedIn: Flow<Boolean> = flowOf(false)
        override fun getCurrentUserId(): String? = null
    }
    AuthScreen(
        viewModel = AuthViewModel(dummyAuthService),
        onNavigateMain = {},
        onNavigatePrivacyPolicy = {}
    )
}

// Add these to satisfy the Preview if needed, though they are not used in code yet
// import android.app.Activity
// import com.google.android.gms.auth.api.signin.GoogleSignIn
// import com.google.android.gms.common.api.ApiException
