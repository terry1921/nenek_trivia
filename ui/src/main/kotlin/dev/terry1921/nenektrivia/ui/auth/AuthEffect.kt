package dev.terry1921.nenektrivia.ui.auth

sealed class AuthEffect {
    data object NavigateToMain : AuthEffect()
    data object NavigateToPrivacyPolicy : AuthEffect()
    data object LaunchGoogleSignIn : AuthEffect()
    data object LaunchFacebookSignIn : AuthEffect()
}
