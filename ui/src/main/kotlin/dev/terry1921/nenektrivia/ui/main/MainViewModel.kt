package dev.terry1921.nenektrivia.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    // Inyecta repositorios de :database cuando estén listos (UserRepository, etc.)
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun setUser(name: String?, avatarUrl: String?) {
        _uiState.value = _uiState.value.copy(displayName = name, avatarUrl = avatarUrl)
    }
}

data class MainUiState(val displayName: String? = null, val avatarUrl: String? = null)
