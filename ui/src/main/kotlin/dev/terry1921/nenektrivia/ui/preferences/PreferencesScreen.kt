package dev.terry1921.nenektrivia.ui.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.components.ActionRow
import dev.terry1921.nenektrivia.ui.components.InfoRow
import dev.terry1921.nenektrivia.ui.components.NavigationRow
import dev.terry1921.nenektrivia.ui.components.SettingsSectionHeader
import dev.terry1921.nenektrivia.ui.components.SwitchRow
import dev.terry1921.nenektrivia.ui.components.ThemeDialog
import dev.terry1921.nenektrivia.ui.tokens.LocalSizeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel,
    onLogoutClicked: () -> Unit,
    onRateAppClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    val spacing = LocalSpacingTokens.current
    val size = LocalSizeTokens.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PreferenceEffect.RateApp -> onRateAppClicked()
                is PreferenceEffect.PrivacyPolicy -> onPrivacyPolicyClicked()
            }
        }
    }

    val state: PreferencesUiState by viewModel.uiState.collectAsState()

    Surface(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = spacing.topAppBarHeight),
                contentPadding = PaddingValues(vertical = size.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(spacing.none)
            ) {
                item { SettingsSectionHeader(stringResource(R.string.sound_appearance_header)) }
                item {
                    SwitchRow(
                        title = stringResource(R.string.music_title),
                        subtitle = stringResource(R.string.music_subtitle),
                        isChecked = state.isMusicEnabled,
                        onCheckedChange = viewModel::onMusicToggle
                    )
                }
                item {
                    SwitchRow(
                        title = stringResource(R.string.vibe_title),
                        subtitle = stringResource(R.string.vibe_subtitle),
                        isChecked = state.isHapticsEnabled,
                        onCheckedChange = viewModel::onHapticsToggle
                    )
                }
                item {
                    NavigationRow(
                        title = stringResource(R.string.theme_title),
                        currentValue = state.selectedTheme.displayName,
                        onClick = viewModel::onThemeClicked
                    )
                }

                item { Spacer(Modifier.height(spacing.large)) }

                item { SettingsSectionHeader(stringResource(R.string.account_header)) }
                item {
                    ActionRow(
                        title = stringResource(R.string.close_session),
                        onClick = onLogoutClicked
                    )
                }

                item { Spacer(Modifier.height(spacing.large)) }

                item { SettingsSectionHeader(stringResource(R.string.about_header)) }
                item {
                    ActionRow(
                        title = stringResource(R.string.playstore_score),
                        onClick = viewModel::onRateClick
                    )
                }
                item {
                    ActionRow(
                        title = stringResource(R.string.privacy_title),
                        onClick = viewModel::onPrivacyPolicyClick
                    )
                }
                item { InfoRow(title = stringResource(R.string.version), value = state.appVersion) }
            }
        }

        if (state.showThemeDialog) {
            ThemeDialog(
                selected = state.selectedTheme,
                onSelect = viewModel::onThemeSelected,
                onDismiss = viewModel::onThemeDialogDismiss
            )
        }
    }
}
