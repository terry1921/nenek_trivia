package dev.terry1921.nenektrivia.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.session.ClearUserSessionUseCase
import javax.inject.Inject
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
class MainViewModel @Inject constructor(private val clearUserSession: ClearUserSessionUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    fun onLogoutClick() {
        if (_uiState.value.isLoggingOut) {
            Timber.d("Logout request ignored because logout is already in progress")
            return
        }
        viewModelScope.launch {
            Timber.d("Logout requested")
            _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }
            runCatching {
                clearUserSession()
            }.onSuccess {
                Timber.i("User session cleared, navigating to auth")
                _uiState.update {
                    it.copy(displayName = null, avatarUrl = null)
                }
                _effect.emit(MainEffect.NavigateToAuth)
            }.onFailure { error ->
                Timber.e(error, "Failed to clear user session during logout")
                _uiState.update { current ->
                    current.copy(errorMessage = "No se pudo cerrar sesión.")
                }
            }
            _uiState.update { it.copy(isLoggingOut = false) }
        }
    }
}
