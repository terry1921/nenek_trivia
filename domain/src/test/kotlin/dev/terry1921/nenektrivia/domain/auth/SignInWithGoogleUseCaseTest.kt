package dev.terry1921.nenektrivia.domain.auth

import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.network.auth.AuthUser
import dev.terry1921.nenektrivia.network.auth.RemoteAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SignInWithGoogleUseCaseTest {

    private val remoteAuthRepository: RemoteAuthRepository = mock()
    private val useCase = SignInWithGoogleUseCase(remoteAuthRepository)

    @Test
    fun invoke_success_mapsAuthUserUsingDisplayName() = runTest {
        whenever(remoteAuthRepository.signInWithGoogle("id-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "google-uid-1",
                    displayName = "Player One",
                    email = "player@example.com",
                    photoUrl = "https://example.com/avatar.png"
                )
            )
        )

        val result = useCase("id-token")

        assertTrue(result.isSuccess)
        assertEquals(
            User(
                id = "google-uid-1",
                username = "Player One",
                photoUrl = "https://example.com/avatar.png"
            ),
            result.getOrNull()
        )
        verify(remoteAuthRepository).signInWithGoogle("id-token")
    }

    @Test
    fun invoke_blankDisplayName_fallsBackToEmail() = runTest {
        whenever(remoteAuthRepository.signInWithGoogle("id-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "google-uid-2",
                    displayName = "   ",
                    email = "fallback@example.com",
                    photoUrl = null
                )
            )
        )

        val result = useCase("id-token")

        assertTrue(result.isSuccess)
        assertEquals("fallback@example.com", result.getOrNull()?.username)
    }

    @Test
    fun invoke_blankDisplayNameAndEmail_fallsBackToUid() = runTest {
        whenever(remoteAuthRepository.signInWithGoogle("id-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "google-uid-3",
                    displayName = null,
                    email = "",
                    photoUrl = null
                )
            )
        )

        val result = useCase("id-token")

        assertTrue(result.isSuccess)
        assertEquals("google-uid-3", result.getOrNull()?.username)
    }

    @Test
    fun invoke_failure_returnsRepositoryFailure() = runTest {
        val error = IllegalStateException("google sign-in failed")
        whenever(
            remoteAuthRepository.signInWithGoogle("id-token")
        ).thenReturn(Result.failure(error))

        val result = useCase("id-token")

        assertTrue(result.isFailure)
        assertSame(error, result.exceptionOrNull())
    }
}
