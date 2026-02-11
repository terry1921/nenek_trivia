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
                val remoteUser = signInWithGoogle(idToken).getOrThrow()
                saveUserSession(remoteUser).getOrThrow()
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                _uiState.update {
                    it.copy(errorMessage = "No se pudo iniciar sesión con Google.")
                }
            } finally {
                _uiState.update { it.copy(isGoogleLoading = false) }
            }
        }
    }

    fun onGoogleCancelled() {
        _uiState.update { it.copy(isGoogleLoading = false) }
    }

    fun onGoogleError(message: String? = null) {
        _uiState.update {
            it.copy(
                isGoogleLoading = false,
                errorMessage = message ?: "No se pudo iniciar sesión con Google."
            )
        }
    }

    fun onFacebookClick() {
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
                val remoteUser = signInWithFacebook(accessToken).getOrThrow()
                saveUserSession(remoteUser).getOrThrow()
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                _uiState.update {
                    it.copy(errorMessage = "No se pudo iniciar sesión con Facebook.")
                }
            } finally {
                _uiState.update { it.copy(isFacebookLoading = false) }
            }
        }
    }

    fun onFacebookCancelled() {
        _uiState.update { it.copy(isFacebookLoading = false) }
    }

    fun onFacebookError(message: String? = null) {
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
        if (getUserSession() != null) {
            _effect.emit(AuthEffect.NavigateToMain)
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _effect.emit(AuthEffect.NavigateToPrivacyPolicy)
        }
    }
}
