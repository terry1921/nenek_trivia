package dev.terry1921.nenektrivia.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    // Inyecta casos de uso cuando esten listos
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun setUser(name: String?, avatarUrl: String?) {
        _uiState.value = _uiState.value.copy(displayName = name, avatarUrl = avatarUrl)
    }

    val userName: String
        get() = _uiState.value.displayName ?: "Invitado"
}
