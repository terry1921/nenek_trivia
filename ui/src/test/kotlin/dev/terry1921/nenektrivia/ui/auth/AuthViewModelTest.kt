package dev.terry1921.nenektrivia.ui.auth

import app.cash.turbine.test
import dev.terry1921.nenektrivia.model.auth.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val authService: AuthService = mock()
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onGoogleSignIn with failure should update error message`() = runTest {
        whenever(authService.signInWithGoogle(any())).doReturn(Result.failure(Exception("Auth Error")))

        viewModel.onGoogleSignIn("token")

        assertEquals("Authentication failed. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onGoogleSignIn with success should emit NavigateToMain effect`() = runTest {
        whenever(authService.signInWithGoogle(any())).doReturn(Result.success(Unit))

        viewModel.effect.test {
            viewModel.onGoogleSignIn("token")
            assertEquals(AuthEffect.NavigateToMain, awaitItem())
        }
    }
}
