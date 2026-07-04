package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.auth.SignInWithFacebookUseCase
import dev.terry1921.nenektrivia.domain.auth.SignInWithGoogleUseCase
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.domain.session.SaveUserSessionUseCase
import dev.terry1921.nenektrivia.database.entity.User
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
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
        Timber.d("Google sign-in requested (Simulated)")
        _uiState.update {
            it.copy(
                isGoogleLoading = true,
                isFacebookLoading = false,
                isGuestLoading = false,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            try {
                delay(1500)
                val user = User(username = "Jugador Google")
                saveUserSession(user).getOrThrow()
                Timber.i("Google sign-in succeeded (Simulated), navigating to main")
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

    fun onGoogleToken(idToken: String) {
        // Obsolete with simulation, but keeping for compatibility if called
        onGoogleClick()
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
        Timber.d("Facebook sign-in requested (Simulated)")
        _uiState.update {
            it.copy(
                isGoogleLoading = false,
                isFacebookLoading = true,
                isGuestLoading = false,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            try {
                delay(1500)
                val user = User(username = "Jugador Facebook")
                saveUserSession(user).getOrThrow()
                Timber.i("Facebook sign-in succeeded (Simulated), navigating to main")
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

    fun onFacebookToken(accessToken: String) {
        // Obsolete with simulation, but keeping for compatibility if called
        onFacebookClick()
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

    fun onGuestClick() {
        Timber.d("Guest sign-in requested (Simulated)")
        _uiState.update {
            it.copy(
                isGoogleLoading = false,
                isFacebookLoading = false,
                isGuestLoading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            try {
                delay(1000)
                val user = User(username = "Invitado")
                saveUserSession(user).getOrThrow()
                Timber.i("Guest sign-in succeeded (Simulated), navigating to main")
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Timber.e(t, "Guest sign-in failed")
                _uiState.update {
                    it.copy(errorMessage = "No se pudo iniciar sesión como invitado.")
                }
            } finally {
                _uiState.update { it.copy(isGuestLoading = false) }
            }
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
