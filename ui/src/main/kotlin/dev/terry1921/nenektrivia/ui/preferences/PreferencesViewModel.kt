package dev.terry1921.nenektrivia.ui.preferences

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
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
import timber.log.Timber

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
        Timber.d("PreferencesViewModel initialized")
        load()
    }

    fun load() = viewModelScope.launch {
        Timber.d("Loading user preferences")
        getSettings()
            .catch { e ->
                Timber.e(e, "Failed to load user preferences")
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
                Timber.i(
                    "User preferences loaded. music=%s, haptics=%s, theme=%s",
                    s.isMusicEnabled,
                    s.isHapticsEnabled,
                    s.theme
                )
            }
    }

    fun onMusicToggle(enabled: Boolean) = viewModelScope.launch {
        Timber.d("Music preference changed: %s", enabled)
        saveSound.setMusic(enabled)
        _uiState.update { it.copy(isMusicEnabled = enabled) }
    }

    fun onHapticsToggle(enabled: Boolean) = viewModelScope.launch {
        Timber.d("Haptics preference changed: %s", enabled)
        saveSound.setHaptics(enabled)
        _uiState.update { it.copy(isHapticsEnabled = enabled) }
    }

    fun onThemeClicked() = viewModelScope.launch {
        Timber.d("Theme picker opened")
        _uiState.update { it.copy(showThemeDialog = true) }
    }

    fun onThemeSelected(theme: Theme) = viewModelScope.launch {
        Timber.i("Theme selected: %s", theme)
        saveTheme(theme)
        _uiState.update { it.copy(selectedTheme = theme, showThemeDialog = false) }
    }

    fun onThemeDialogDismiss() {
        Timber.d("Theme picker dismissed")
        _uiState.update { it.copy(showThemeDialog = false) }
    }

    fun onPrivacyPolicyClick() = viewModelScope.launch {
        Timber.d("Privacy policy requested from preferences")
        _effect.emit(PreferenceEffect.PrivacyPolicy)
    }

    fun onRateClick() = viewModelScope.launch {
        Timber.d("Rate app requested from preferences")
        _effect.emit(PreferenceEffect.RateApp)
    }

    private fun requestReviewInfo() {
        Timber.d("Requesting in-app review info")
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.reviewInfo = task.result
                Timber.d("In-app review info received")
            } else {
                Timber.e(task.exception, "Error requesting in-app review flow")
            }
        }
    }

    fun startInAppReview(activity: Activity) {
        val currentReviewInfo = reviewInfo
        if (currentReviewInfo != null) {
            Timber.d("Launching in-app review flow")
            val flow = reviewManager.launchReviewFlow(activity, currentReviewInfo)
            flow.addOnCompleteListener { _ ->
                Timber.d("In-app review flow completed")
                this.reviewInfo = null
                requestReviewInfo()
            }
        } else {
            Timber.w("In-app review info unavailable, opening Play Store fallback")
            openPlayStoreForReview(activity)
        }
    }

    private fun openPlayStoreForReview(context: Context) {
        val packageName = context.packageName
        try {
            Timber.d("Opening Play Store review page via market URI")
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            Timber.w(e, "Market app unavailable, opening Play Store review page in browser")
            val playStoreUrl = "https://play.google.com/store/apps/details?id=$packageName".toUri()
            CustomTabsIntent.Builder().build().launchUrl(context, playStoreUrl)
        }
    }
}
