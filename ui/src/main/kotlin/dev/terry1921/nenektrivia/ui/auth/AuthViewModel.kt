package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.model.auth.AuthProvider
import dev.terry1921.nenektrivia.model.auth.AuthService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isFacebookLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class AuthEffect {
    data object NavigateToMain : AuthEffect()
    data object NavigateToPrivacyPolicy : AuthEffect()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    /**
     * Triggered when the Google login button is clicked.
     * The token should be obtained from the Google Sign-In SDK.
     */
    fun onGoogleSignIn(idToken: String) {
        performLogin(AuthProvider.GOOGLE, idToken)
    }

    /**
     * Triggered when the Facebook login button is clicked.
     * The token should be obtained from the Facebook Login SDK.
     */
    fun onFacebookSignIn(accessToken: String) {
        performLogin(AuthProvider.FACEBOOK, accessToken)
    }

    private fun performLogin(provider: AuthProvider, token: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGoogleLoading = provider == AuthProvider.GOOGLE,
                    isFacebookLoading = provider == AuthProvider.FACEBOOK,
                    errorMessage = null
                )
            }
            try {
                // Using the real auth service eliminates the simulation bypass.
                // Firebase will validate the provided token.
                val result = when (provider) {
                    AuthProvider.GOOGLE -> authService.signInWithGoogle(token)
                    AuthProvider.FACEBOOK -> authService.signInWithFacebook(token)
                }

                if (result.isSuccess) {
                    _effect.emit(AuthEffect.NavigateToMain)
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Authentication failed. Please try again."
                        )
                    }
                }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(
                        errorMessage = "An unexpected error occurred. Please try again."
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
