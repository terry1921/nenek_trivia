package dev.terry1921.nenektrivia.ui.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PreferencesRoute(
    viewModel: PreferencesViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onRate: () -> Unit = {},
    onPrivacy: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    PreferencesScreen(
        state = state,
        onMusicToggle = viewModel::onMusicToggle,
        onHapticsToggle = viewModel::onHapticsToggle,
        onChangeThemeClicked = viewModel::onThemeClicked,
        onThemeSelected = viewModel::onThemeSelected,
        onDismissThemeDialog = viewModel::onThemeDialogDismiss,
        onLogoutClicked = onLogout,
        onRateAppClicked = onRate,
        onPrivacyPolicyClicked = onPrivacy
    )
}
