package dev.terry1921.nenektrivia.ui.preferences

import dev.terry1921.nenektrivia.domain.account.AccountDeletionRequest
import dev.terry1921.nenektrivia.domain.account.DeleteAccountUseCase
import dev.terry1921.nenektrivia.network.auth.AccountProvider
import dev.terry1921.nenektrivia.ui.auth.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AccountDeletionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val deleteAccount: DeleteAccountUseCase = mock()
    private val request = AccountDeletionRequest("player-1", setOf(AccountProvider.GOOGLE))

    @Test
    fun cancellingVerificationDoesNotDeleteAccount() = runTest {
        whenever(deleteAccount.finishInterruptedCleanup()).thenReturn(false)
        whenever(deleteAccount.request()).thenReturn(request)
        val viewModel = AccountDeletionViewModel(deleteAccount)

        viewModel.open()
        advanceUntilIdle()
        viewModel.verify(AccountProvider.GOOGLE) {}
        viewModel.onVerificationCancelled()
        viewModel.onCredential(AccountProvider.GOOGLE, "late-token")
        advanceUntilIdle()

        assertEquals(AccountDeletionState.Confirm(request), viewModel.state.value)
        verify(deleteAccount, never()).invoke(any(), any(), any())
    }

    @Test
    fun confirmedCredentialDeletesOnlyTheAccountShownInTheRequest() = runTest {
        whenever(deleteAccount.finishInterruptedCleanup()).thenReturn(false)
        whenever(deleteAccount.request()).thenReturn(request)
        val viewModel = AccountDeletionViewModel(deleteAccount)

        viewModel.open()
        advanceUntilIdle()
        viewModel.verify(AccountProvider.GOOGLE) {}
        viewModel.onCredential(AccountProvider.FACEBOOK, "wrong-provider")
        viewModel.onCredential(AccountProvider.GOOGLE, "verified-token")
        advanceUntilIdle()

        assertEquals(AccountDeletionState.Finished, viewModel.state.value)
        verify(deleteAccount, never()).invoke(any(), eq(AccountProvider.FACEBOOK), any())
        verify(deleteAccount).invoke("player-1", AccountProvider.GOOGLE, "verified-token")
    }
}
