package dev.terry1921.nenektrivia.ui.preferences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.terry1921.nenektrivia.domain.preferences.GetUserSettingsUseCase
import dev.terry1921.nenektrivia.domain.preferences.SaveSoundPrefsUseCase
import dev.terry1921.nenektrivia.domain.preferences.SaveThemeUseCase
import dev.terry1921.nenektrivia.model.category.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val getSettings: GetUserSettingsUseCase,
    private val saveTheme: SaveThemeUseCase,
    private val saveSound: SaveSoundPrefsUseCase
) : ViewModel() {

    val reviewManager = ReviewManagerFactory.create(applicationContext)
    private var reviewInfo: ReviewInfo? = null

    private val _uiState = MutableStateFlow(PreferencesUiState(isLoading = true))
    val uiState: StateFlow<PreferencesUiState> = _uiState

    private val _effect = MutableSharedFlow<PreferenceEffect>()
    val effect: SharedFlow<PreferenceEffect> = _effect.asSharedFlow()

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

    fun onPrivacyPolicyClick() = viewModelScope.launch {
        _effect.emit(PreferenceEffect.PrivacyPolicy)
    }

    fun onRateClick() = viewModelScope.launch {
        _effect.emit(PreferenceEffect.RateApp)
    }

    private fun requestReviewInfo() {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.reviewInfo = task.result
            } else {
                Log.e("InAppReview", "Error requesting review flow", task.exception)
            }
        }
    }

    fun startInAppReview(activity: Activity) {
        val currentReviewInfo = reviewInfo
        if (currentReviewInfo != null) {
            val flow = reviewManager.launchReviewFlow(activity, currentReviewInfo)
            flow.addOnCompleteListener { _ ->
                this.reviewInfo = null
                requestReviewInfo()
            }
        } else {
            openPlayStoreForReview(activity) // Example fallback
        }
    }

    // Fallback method (same as before)
    private fun openPlayStoreForReview(context: Context) {
        val packageName = "com.fenixarts.nenektrivia" // context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
            )
        }
    }
}
