package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.entity.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveUserSessionUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val useCase = SaveUserSessionUseCase(authRepository)

    @Test
    fun invoke_existingUserById_setsSessionAndReturnsUser() = runTest {
        val user = User(id = "firebase-uid-1", username = "Jugador")
        whenever(authRepository.getUserById("firebase-uid-1")).thenReturn(user)

        val result = useCase(user)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
        verify(authRepository).setActiveSession("firebase-uid-1")
        verify(authRepository, never()).registerUser(any())
    }

    @Test
    fun invoke_registerFailsButUsernameExists_usesFallbackUser() = runTest {
        val incoming = User(id = "firebase-uid-2", username = "Jugador")
        val existing = User(id = "local-user-id", username = "Jugador")
        whenever(authRepository.getUserById("firebase-uid-2")).thenReturn(null)
        whenever(authRepository.registerUser(incoming)).thenReturn(
            Result.failure(IllegalStateException("conflict"))
        )
        whenever(authRepository.getUserByUsername("Jugador")).thenReturn(existing)

        val result = useCase(incoming)

        assertTrue(result.isSuccess)
        assertEquals(existing, result.getOrNull())
        verify(authRepository).setActiveSession("local-user-id")
    }

    @Test
    fun invoke_registerFailsAndNoFallback_returnsFailure() = runTest {
        val incoming = User(id = "firebase-uid-3", username = "Jugador")
        whenever(authRepository.getUserById("firebase-uid-3")).thenReturn(null)
        whenever(authRepository.registerUser(incoming)).thenReturn(
            Result.failure(IllegalStateException("insert error"))
        )
        whenever(authRepository.getUserByUsername("Jugador")).thenReturn(null)

        val result = useCase(incoming)

        assertTrue(result.isFailure)
        verify(authRepository, never()).setActiveSession(any())
    }
}
