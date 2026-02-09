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

@HiltViewModel
class MainViewModel @Inject constructor(private val clearUserSession: ClearUserSessionUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    fun setUser(name: String?, avatarUrl: String?) {
        _uiState.value = _uiState.value.copy(displayName = name, avatarUrl = avatarUrl)
    }

    val userName: String
        get() = _uiState.value.displayName ?: "Invitado"

    fun onLogoutClick() {
        if (_uiState.value.isLoggingOut) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }
            runCatching {
                clearUserSession()
            }.onSuccess {
                _effect.emit(MainEffect.NavigateToAuth)
            }.onFailure {
                _uiState.update { current ->
                    current.copy(errorMessage = "No se pudo cerrar sesión.")
                }
            }
            _uiState.update { it.copy(isLoggingOut = false) }
        }
    }
}
