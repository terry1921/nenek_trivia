package dev.terry1921.nenektrivia.ui.profile

import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.model.category.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getUserSession: GetUserSessionUseCase = mock()

    @Test
    fun load_withActiveUser_updatesUiState() = runTest {
        whenever(getUserSession.invoke()).thenReturn(
            User(username = "Player One", photoUrl = "https://example.com/avatar.png")
        )

        val viewModel = ProfileViewModel(getUserSession)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Player One", state.displayName)
        assertEquals("https://example.com/avatar.png", state.avatarUrl)
        assertNull(state.error)
        assertEquals(
            linkedMapOf(
                Category.Art to 68,
                Category.Sports to 80,
                Category.General to 45,
                Category.Geography to 90,
                Category.History to 12
            ),
            state.knowledge
        )
    }

    @Test
    fun load_withNoActiveUser_showsError() = runTest {
        whenever(getUserSession.invoke()).thenReturn(null)

        val viewModel = ProfileViewModel(getUserSession)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("No active user session found.", state.error)
        assertTrue(state.knowledge.isEmpty())
        assertNull(state.displayName)
        assertNull(state.avatarUrl)
    }

    @Test
    fun load_whenException_showsErrorMessage() = runTest {
        whenever(getUserSession.invoke()).thenThrow(IllegalStateException("Boom"))

        val viewModel = ProfileViewModel(getUserSession)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Boom", state.error)
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
