package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.auth.SignInWithFacebookUseCase
import dev.terry1921.nenektrivia.domain.auth.SignInWithGoogleUseCase
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.domain.session.SaveUserSessionUseCase
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val saveUserSession: SaveUserSessionUseCase,
    private val getUserSession: GetUserSessionUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val signInWithFacebook: SignInWithFacebookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun onGoogleClick() {
        Timber.d("Google sign-in requested")
        _uiState.update {
            it.copy(
                isGoogleLoading = true,
                isFacebookLoading = false,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            _effect.emit(AuthEffect.LaunchGoogleSignIn)
        }
    }

    fun onGoogleToken(idToken: String) {
        viewModelScope.launch {
            try {
                Timber.d("Google sign-in started")
                val remoteUser = signInWithGoogle(idToken).getOrThrow()
                saveUserSession(remoteUser).getOrThrow()
                Timber.i("Google sign-in succeeded, navigating to main")
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Timber.e(t, "Google sign-in failed")
                _uiState.update {
                    it.copy(errorMessage = "No se pudo iniciar sesión con Google.")
                }
            } finally {
                _uiState.update { it.copy(isGoogleLoading = false) }
            }
        }
    }

    fun onGoogleCancelled() {
        Timber.i("Google sign-in cancelled")
        _uiState.update { it.copy(isGoogleLoading = false) }
    }

    fun onGoogleError(message: String? = null) {
        Timber.w("Google sign-in returned an error: %s", message ?: "unknown")
        _uiState.update {
            it.copy(
                isGoogleLoading = false,
                errorMessage = message ?: "No se pudo iniciar sesión con Google."
            )
        }
    }

    fun onFacebookClick() {
        Timber.d("Facebook sign-in requested")
        _uiState.update {
            it.copy(
                isGoogleLoading = false,
                isFacebookLoading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            _effect.emit(AuthEffect.LaunchFacebookSignIn)
        }
    }

    fun onFacebookToken(accessToken: String) {
        viewModelScope.launch {
            try {
                Timber.d("Facebook sign-in started")
                val remoteUser = signInWithFacebook(accessToken).getOrThrow()
                saveUserSession(remoteUser).getOrThrow()
                Timber.i("Facebook sign-in succeeded, navigating to main")
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Timber.e(t, "Facebook sign-in failed")
                _uiState.update {
                    it.copy(errorMessage = "No se pudo iniciar sesión con Facebook.")
                }
            } finally {
                _uiState.update { it.copy(isFacebookLoading = false) }
            }
        }
    }

    fun onFacebookCancelled() {
        Timber.i("Facebook sign-in cancelled")
        _uiState.update { it.copy(isFacebookLoading = false) }
    }

    fun onFacebookError(message: String? = null) {
        Timber.w("Facebook sign-in returned an error: %s", message ?: "unknown")
        _uiState.update {
            it.copy(
                isFacebookLoading = false,
                errorMessage = message ?: "No se pudo iniciar sesión con Facebook."
            )
        }
    }

    init {
        checkIfUserIsLoggedIn()
    }

    fun checkIfUserIsLoggedIn() = viewModelScope.launch {
        Timber.d("Checking for existing user session")
        if (getUserSession() != null) {
            Timber.i("Existing user session found, navigating to main")
            _effect.emit(AuthEffect.NavigateToMain)
        } else {
            Timber.d("No existing user session found")
        }
    }

    fun onPrivacyPolicyClick() {
        Timber.d("Privacy policy requested from auth screen")
        viewModelScope.launch {
            _effect.emit(AuthEffect.NavigateToPrivacyPolicy)
        }
    }
}
