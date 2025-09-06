package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isFacebookLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class AuthEffect {
    data object NavigateToMain : AuthEffect()
    data object NavigateToPrivacyPolicy : AuthEffect()
}

enum class AuthProvider { GOOGLE, FACEBOOK }

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun onGoogleClick() = simulateLogin(AuthProvider.GOOGLE)

    fun onFacebookClick() = simulateLogin(AuthProvider.FACEBOOK)

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
