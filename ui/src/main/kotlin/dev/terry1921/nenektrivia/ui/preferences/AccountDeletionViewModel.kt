package dev.terry1921.nenektrivia.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.account.AccountDeletionRequest
import dev.terry1921.nenektrivia.domain.account.DeleteAccountUseCase
import dev.terry1921.nenektrivia.network.auth.AccountProvider
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal sealed interface AccountDeletionState {
    data object Idle : AccountDeletionState
    data object Loading : AccountDeletionState
    data class Confirm(val request: AccountDeletionRequest) : AccountDeletionState
    data class Verifying(val request: AccountDeletionRequest, val provider: AccountProvider) :
        AccountDeletionState
    data object Deleting : AccountDeletionState
    data object Failed : AccountDeletionState
    data object Finished : AccountDeletionState
}

@HiltViewModel
internal class AccountDeletionViewModel @Inject constructor(
    private val deleteAccount: DeleteAccountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<AccountDeletionState>(AccountDeletionState.Idle)
    val state = _state.asStateFlow()

    fun open() {
        if (_state.value !in setOf(AccountDeletionState.Idle, AccountDeletionState.Failed)) return
        _state.value = AccountDeletionState.Loading
        viewModelScope.launch {
            _state.value = try {
                if (deleteAccount.finishInterruptedCleanup()) {
                    AccountDeletionState.Finished
                } else {
                    val request = deleteAccount.request()
                    check(request.providers.isNotEmpty())
                    AccountDeletionState.Confirm(request)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                AccountDeletionState.Failed
            }
        }
    }

    fun verify(provider: AccountProvider, launch: () -> Unit) {
        val state = _state.value as? AccountDeletionState.Confirm ?: return
        if (provider !in state.request.providers) return
        _state.value = AccountDeletionState.Verifying(state.request, provider)
        launch()
    }

    fun onCredential(provider: AccountProvider, token: String) {
        val state = _state.value as? AccountDeletionState.Verifying ?: return
        if (state.provider != provider) return
        _state.value = AccountDeletionState.Deleting
        viewModelScope.launch {
            _state.value = try {
                deleteAccount(state.request.uid, provider, token)
                AccountDeletionState.Finished
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                AccountDeletionState.Failed
            }
        }
    }

    fun onVerificationCancelled() {
        val state = _state.value as? AccountDeletionState.Verifying ?: return
        _state.value = AccountDeletionState.Confirm(state.request)
    }

    fun onVerificationError() {
        if (_state.value is AccountDeletionState.Verifying) {
            _state.value =
                AccountDeletionState.Failed
        }
    }

    fun dismiss() {
        when (_state.value) {
            is AccountDeletionState.Confirm, AccountDeletionState.Failed -> {
                _state.value = AccountDeletionState.Idle
            }

            else -> Unit
        }
    }
}
