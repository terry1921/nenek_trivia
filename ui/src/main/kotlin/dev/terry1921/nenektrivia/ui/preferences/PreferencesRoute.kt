package dev.terry1921.nenektrivia.ui.preferences

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PreferencesRoute(
    viewModel: PreferencesViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onRate: () -> Unit = {},
    onPrivacy: () -> Unit = {}
) {
    PreferencesScreen(
        viewModel = viewModel,
        onLogoutClicked = onLogout,
        onRateAppClicked = onRate,
        onPrivacyPolicyClicked = onPrivacy
    )
}
