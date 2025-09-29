package dev.terry1921.nenektrivia.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.preferences.GetUserSettingsUseCase
import dev.terry1921.nenektrivia.domain.preferences.SaveSoundPrefsUseCase
import dev.terry1921.nenektrivia.domain.preferences.SaveThemeUseCase
import dev.terry1921.nenektrivia.model.category.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val getSettings: GetUserSettingsUseCase,
    private val saveTheme: SaveThemeUseCase,
    private val saveSound: SaveSoundPrefsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesUiState(isLoading = true))
    val uiState: StateFlow<PreferencesUiState> = _uiState

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        getSettings()
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error cargando preferencias"
                    )
                }
            }
            .collect { s ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isMusicEnabled = s.isMusicEnabled,
                        isHapticsEnabled = s.isHapticsEnabled,
                        selectedTheme = s.theme,
                        error = null
                    )
                }
            }
    }

    fun onMusicToggle(enabled: Boolean) = viewModelScope.launch {
        saveSound.setMusic(enabled)
        _uiState.update { it.copy(isMusicEnabled = enabled) }
    }

    fun onHapticsToggle(enabled: Boolean) = viewModelScope.launch {
        saveSound.setHaptics(enabled)
        _uiState.update { it.copy(isHapticsEnabled = enabled) }
    }

    fun onThemeClicked() = viewModelScope.launch {
        _uiState.update { it.copy(showThemeDialog = true) }
    }

    fun onThemeSelected(theme: Theme) = viewModelScope.launch {
        saveTheme(theme)
        _uiState.update { it.copy(selectedTheme = theme, showThemeDialog = false) }
    }

    fun onThemeDialogDismiss() {
        _uiState.update { it.copy(showThemeDialog = false) }
    }
}
