package dev.terry1921.nenektrivia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.domain.session.SaveUserSessionUseCase
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
class AuthViewModel @Inject constructor(
    private val saveUserSession: SaveUserSessionUseCase,
    private val getUserSession: GetUserSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun onGoogleClick() = simulateLogin(AuthProvider.GOOGLE)

    fun onFacebookClick() = simulateLogin(AuthProvider.FACEBOOK)

    init {
        checkIfUserIsLoggedIn()
    }

    fun checkIfUserIsLoggedIn() = viewModelScope.launch {
        if (getUserSession() != null) {
            _effect.emit(AuthEffect.NavigateToMain)
        }
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
                saveUserSession(simulatedUser(provider)).getOrThrow()
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

    private fun simulatedUser(provider: AuthProvider): User = when (provider) {
        AuthProvider.GOOGLE ->
            User(
                username = "Jugador Google",
                photoUrl = "https://i.pravatar.cc/300?img=12"
            )

        AuthProvider.FACEBOOK ->
            User(
                username = "Jugador Facebook",
                photoUrl = "https://i.pravatar.cc/300?img=32"
            )
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _effect.emit(AuthEffect.NavigateToPrivacyPolicy)
        }
    }
}
