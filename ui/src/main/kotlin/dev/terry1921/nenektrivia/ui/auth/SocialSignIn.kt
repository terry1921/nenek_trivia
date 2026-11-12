package dev.terry1921.nenektrivia.ui.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dev.terry1921.nenektrivia.ui.R

private val FacebookPermissions = listOf("email", "public_profile")

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

internal class SocialSignInActions(val launchGoogle: () -> Unit, val launchFacebook: () -> Unit)

internal class SocialSignInCallbacks(
    val onGoogleToken: (String) -> Unit,
    val onGoogleCancelled: () -> Unit,
    val onGoogleError: (String?) -> Unit,
    val onFacebookToken: (String) -> Unit,
    val onFacebookCancelled: () -> Unit,
    val onFacebookError: (String?) -> Unit
)

@Composable
internal fun rememberSocialSignIn(events: SocialSignInCallbacks): SocialSignInActions {
    val callbacks by rememberUpdatedState(events)
    val context = LocalContext.current
    val callbackManager = remember { CallbackManager.Factory.create() }
    val clientId = stringResource(R.string.default_web_client_id)
    val googleSignInClient = remember {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context, gso)
    }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            callbacks.onGoogleCancelled()
            return@rememberLauncherForActivityResult
        }
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken.isNullOrBlank()) {
                callbacks.onGoogleError("No se pudo obtener el token de Google.")
            } else {
                callbacks.onGoogleToken(idToken)
            }
        } catch (error: ApiException) {
            callbacks.onGoogleError(null)
        }
    }

    DisposableEffect(callbackManager) {
        val loginManager = LoginManager.getInstance()
        val facebookCallback =
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken.token
                    if (token.isBlank()) {
                        callbacks.onFacebookError("No se pudo obtener el token de Facebook.")
                    } else {
                        callbacks.onFacebookToken(token)
                    }
                }

                override fun onCancel() {
                    callbacks.onFacebookCancelled()
                }

                override fun onError(error: FacebookException) {
                    callbacks.onFacebookError(error.localizedMessage)
                }
            }
        loginManager.registerCallback(callbackManager, facebookCallback)
        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

    return SocialSignInActions(
        launchGoogle = {
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        },
        launchFacebook = {
            val activity = context.findActivity()
            val activityResultOwner = activity as? ActivityResultRegistryOwner
            if (activityResultOwner == null) {
                callbacks.onFacebookError("No se pudo abrir el login de Facebook.")
            } else {
                try {
                    LoginManager
                        .getInstance()
                        .logInWithReadPermissions(
                            activityResultOwner,
                            callbackManager,
                            FacebookPermissions
                        )
                } catch (t: Exception) {
                    val message = t.message?.takeIf { it.isNotBlank() }
                    callbacks.onFacebookError(
                        message ?: "No se pudo iniciar sesión con Facebook."
                    )
                }
            }
        }
    )
}
