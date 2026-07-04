package dev.terry1921.nenektrivia.ui.auth

import app.cash.turbine.test
import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.domain.auth.SignInWithFacebookUseCase
import dev.terry1921.nenektrivia.domain.auth.SignInWithGoogleUseCase
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.domain.session.SaveUserSessionUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveUserSession: SaveUserSessionUseCase = mock()
    private val getUserSession: GetUserSessionUseCase = mock()
    private val signInWithGoogle: SignInWithGoogleUseCase = mock()
    private val signInWithFacebook: SignInWithFacebookUseCase = mock()

    @Test
    fun checkIfUserIsLoggedIn_emitsNavigateToMain() = runTest {
        whenever(getUserSession.invoke()).thenReturn(User(username = "Existing"))

        val viewModel = createViewModel()

        viewModel.effect.test {
            advanceUntilIdle()
            assertEquals(AuthEffect.NavigateToMain, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGoogleClick_setsLoadingAndNavigates() = runTest {
        whenever(getUserSession.invoke()).thenReturn(null)
        whenever(saveUserSession.invoke(any())).thenAnswer { Result.success(it.arguments[0] as User) }

        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onGoogleClick()
            runCurrent()

            assertTrue(viewModel.uiState.value.isGoogleLoading)
            assertFalse(viewModel.uiState.value.isFacebookLoading)
            assertNull(viewModel.uiState.value.errorMessage)

            advanceUntilIdle()

            assertEquals(AuthEffect.NavigateToMain, awaitItem())
            assertFalse(viewModel.uiState.value.isGoogleLoading)

            verify(saveUserSession).invoke(
                check {
                    assertEquals("Jugador Google", it.username)
                }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onFacebookClick_setsLoadingAndNavigates() = runTest {
        whenever(getUserSession.invoke()).thenReturn(null)
        whenever(saveUserSession.invoke(any())).thenAnswer { Result.success(it.arguments[0] as User) }

        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onFacebookClick()
            runCurrent()

            assertFalse(viewModel.uiState.value.isGoogleLoading)
            assertTrue(viewModel.uiState.value.isFacebookLoading)
            assertNull(viewModel.uiState.value.errorMessage)

            advanceUntilIdle()

            assertEquals(AuthEffect.NavigateToMain, awaitItem())
            assertFalse(viewModel.uiState.value.isFacebookLoading)

            verify(saveUserSession).invoke(
                check {
                    assertEquals("Jugador Facebook", it.username)
                }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onGuestClick_setsLoadingAndNavigates() = runTest {
        whenever(getUserSession.invoke()).thenReturn(null)
        whenever(saveUserSession.invoke(any())).thenAnswer { Result.success(it.arguments[0] as User) }

        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onGuestClick()
            runCurrent()

            assertFalse(viewModel.uiState.value.isGoogleLoading)
            assertFalse(viewModel.uiState.value.isFacebookLoading)
            assertTrue(viewModel.uiState.value.isGuestLoading)
            assertNull(viewModel.uiState.value.errorMessage)

            advanceUntilIdle()

            assertEquals(AuthEffect.NavigateToMain, awaitItem())
            assertFalse(viewModel.uiState.value.isGuestLoading)

            verify(saveUserSession).invoke(
                check {
                    assertEquals("Invitado", it.username)
                }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onPrivacyPolicyClick_emitsNavigateToPrivacyPolicy() = runTest {
        whenever(getUserSession.invoke()).thenReturn(null)

        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onPrivacyPolicyClick()
            runCurrent()

            assertEquals(AuthEffect.NavigateToPrivacyPolicy, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() = AuthViewModel(
        saveUserSession = saveUserSession,
        getUserSession = getUserSession,
        signInWithGoogle = signInWithGoogle,
        signInWithFacebook = signInWithFacebook
    )
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
