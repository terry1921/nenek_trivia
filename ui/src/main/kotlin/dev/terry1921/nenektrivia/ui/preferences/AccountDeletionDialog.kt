package dev.terry1921.nenektrivia.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import dev.terry1921.nenektrivia.network.auth.AccountProvider
import dev.terry1921.nenektrivia.ui.R
import dev.terry1921.nenektrivia.ui.auth.SocialSignInCallbacks
import dev.terry1921.nenektrivia.ui.auth.rememberSocialSignIn

@Composable
internal fun AccountDeletionDialog(viewModel: AccountDeletionViewModel, onDeleted: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val signIn = rememberSocialSignIn(
        SocialSignInCallbacks(
            onGoogleToken = { viewModel.onCredential(AccountProvider.GOOGLE, it) },
            onGoogleCancelled = viewModel::onVerificationCancelled,
            onGoogleError = { viewModel.onVerificationError() },
            onFacebookToken = { viewModel.onCredential(AccountProvider.FACEBOOK, it) },
            onFacebookCancelled = viewModel::onVerificationCancelled,
            onFacebookError = { viewModel.onVerificationError() }
        )
    )

    LaunchedEffect(state) {
        if (state == AccountDeletionState.Finished) onDeleted()
    }

    val current = state
    if (current == AccountDeletionState.Idle || current == AccountDeletionState.Finished) return
    val canDismiss =
        current is AccountDeletionState.Confirm || current == AccountDeletionState.Failed
    AlertDialog(
        onDismissRequest = viewModel::dismiss,
        title = { Text(stringResource(R.string.delete_account)) },
        text = {
            when (current) {
                is AccountDeletionState.Confirm -> Text(
                    stringResource(R.string.delete_account_confirmation)
                )

                AccountDeletionState.Failed -> Text(stringResource(R.string.delete_account_error))

                else -> Column {
                    CircularProgressIndicator()
                    Text(stringResource(R.string.delete_account_wait))
                }
            }
        },
        confirmButton = {
            when (current) {
                is AccountDeletionState.Confirm -> Column {
                    if (AccountProvider.GOOGLE in current.request.providers) {
                        TextButton(onClick = {
                            viewModel.verify(AccountProvider.GOOGLE, signIn.launchGoogle)
                        }) {
                            Text(stringResource(R.string.delete_account_google))
                        }
                    }
                    if (AccountProvider.FACEBOOK in current.request.providers) {
                        TextButton(onClick = {
                            viewModel.verify(AccountProvider.FACEBOOK, signIn.launchFacebook)
                        }) {
                            Text(stringResource(R.string.delete_account_facebook))
                        }
                    }
                }

                AccountDeletionState.Failed -> TextButton(onClick = viewModel::open) {
                    Text(stringResource(R.string.delete_account_retry))
                }

                else -> Unit
            }
        },
        dismissButton = {
            if (canDismiss) {
                TextButton(onClick = viewModel::dismiss) {
                    Text(stringResource(R.string.delete_account_cancel))
                }
            }
        }
    )
}
