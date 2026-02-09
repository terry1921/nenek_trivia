package dev.terry1921.nenektrivia.ui.preferences

sealed class PreferenceEffect {
    data object RateApp : PreferenceEffect()
    data object PrivacyPolicy : PreferenceEffect()
}
