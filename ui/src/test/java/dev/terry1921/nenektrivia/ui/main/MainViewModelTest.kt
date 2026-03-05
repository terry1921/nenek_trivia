package dev.terry1921.nenektrivia.ui.main

import app.cash.turbine.test
import dev.terry1921.nenektrivia.domain.session.ClearUserSessionUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val clearUserSession: ClearUserSessionUseCase = mock()

    @Test
    fun onLogoutClick_success_clearsSessionAndNavigates() = runTest {
        val gate = CompletableDeferred<Unit>()
        whenever { clearUserSession.invoke() } doSuspendableAnswer {
            gate.await()
            Unit
        }
        val viewModel = MainViewModel(clearUserSession)

        viewModel.effect.test {
            viewModel.onLogoutClick()

            // Check loading state
            runCurrent()
            assertTrue(viewModel.uiState.value.isLoggingOut)

            gate.complete(Unit)
            advanceUntilIdle()

            // Check final state
            assertFalse(viewModel.uiState.value.isLoggingOut)
            assertNull(viewModel.uiState.value.displayName)
            assertNull(viewModel.uiState.value.avatarUrl)
            assertNull(viewModel.uiState.value.errorMessage)

            // Check effect
            assertEquals(MainEffect.NavigateToAuth, awaitItem())

            verify(clearUserSession).invoke()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onLogoutClick_failure_showsErrorMessage() = runTest {
        val gate = CompletableDeferred<Unit>()
        whenever { clearUserSession.invoke() } doSuspendableAnswer {
            gate.await()
            throw RuntimeException("Logout failed")
        }
        val viewModel = MainViewModel(clearUserSession)

        viewModel.onLogoutClick()

        // Check loading state
        runCurrent()
        assertTrue(viewModel.uiState.value.isLoggingOut)

        gate.complete(Unit)
        advanceUntilIdle()

        // Check final state
        assertFalse(viewModel.uiState.value.isLoggingOut)
        assertEquals("No se pudo cerrar sesión.", viewModel.uiState.value.errorMessage)

        verify(clearUserSession).invoke()
    }

    @Test
    fun onLogoutClick_alreadyLoggingOut_doesNothing() = runTest {
        val gate = CompletableDeferred<Unit>()
        whenever { clearUserSession.invoke() } doSuspendableAnswer {
            gate.await()
            Unit
        }

        val viewModel = MainViewModel(clearUserSession)

        viewModel.onLogoutClick()
        runCurrent()
        assertTrue(viewModel.uiState.value.isLoggingOut)

        // Second click while first is still running
        viewModel.onLogoutClick()
        runCurrent()

        gate.complete(Unit)
        advanceUntilIdle()

        // Should only have called clearUserSession once
        verify(clearUserSession, times(1)).invoke()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
