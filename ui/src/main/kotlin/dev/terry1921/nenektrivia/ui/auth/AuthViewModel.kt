package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun onGoogleClick() = simulateLogin(AuthProvider.GOOGLE)

    fun onFacebookClick() = simulateLogin(AuthProvider.FACEBOOK)

    init {
        checkIfUserIsLoggedIn()
    }

    // TODO() -> Implementar verificación de usuario logueado con database and FireAuth.
    fun checkIfUserIsLoggedIn() = viewModelScope.launch {
        delay(10)
        _effect.emit(AuthEffect.NavigateToMain)
    }

    private fun simulateLogin(provider: AuthProvider) {
        // Conexión real a Firebase quedará fuera de alcance por ahora.
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGoogleLoading = provider == AuthProvider.GOOGLE,
                    isFacebookLoading = provider == AuthProvider.FACEBOOK,
                    errorMessage = null
                )
            }
            try {
                // TODO() -> integrar FirebaseAuth con el provider cuando corresponda.
                delay(1200)
                _effect.emit(AuthEffect.NavigateToMain)
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        isFacebookLoading = false,
                        errorMessage = "Ocurrió un error. Inténtalo de nuevo."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        isFacebookLoading = false
                    )
                }
            }
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _effect.emit(AuthEffect.NavigateToPrivacyPolicy)
        }
    }
}
