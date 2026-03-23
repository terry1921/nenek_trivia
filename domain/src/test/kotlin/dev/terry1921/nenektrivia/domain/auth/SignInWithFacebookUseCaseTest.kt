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

class SignInWithFacebookUseCaseTest {

    private val remoteAuthRepository: RemoteAuthRepository = mock()
    private val useCase = SignInWithFacebookUseCase(remoteAuthRepository)

    @Test
    fun invoke_success_mapsAuthUserUsingDisplayName() = runTest {
        whenever(remoteAuthRepository.signInWithFacebook("access-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "facebook-uid-1",
                    displayName = "Player One",
                    email = "player@example.com",
                    photoUrl = "https://example.com/avatar.png"
                )
            )
        )

        val result = useCase("access-token")

        assertTrue(result.isSuccess)
        assertEquals(
            User(
                id = "facebook-uid-1",
                username = "Player One",
                photoUrl = "https://example.com/avatar.png"
            ),
            result.getOrNull()
        )
        verify(remoteAuthRepository).signInWithFacebook("access-token")
    }

    @Test
    fun invoke_blankDisplayName_fallsBackToEmail() = runTest {
        whenever(remoteAuthRepository.signInWithFacebook("access-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "facebook-uid-2",
                    displayName = "  ",
                    email = "fallback@example.com",
                    photoUrl = null
                )
            )
        )

        val result = useCase("access-token")

        assertTrue(result.isSuccess)
        assertEquals("fallback@example.com", result.getOrNull()?.username)
    }

    @Test
    fun invoke_blankDisplayNameAndEmail_fallsBackToUid() = runTest {
        whenever(remoteAuthRepository.signInWithFacebook("access-token")).thenReturn(
            Result.success(
                AuthUser(
                    uid = "facebook-uid-3",
                    displayName = null,
                    email = "",
                    photoUrl = null
                )
            )
        )

        val result = useCase("access-token")

        assertTrue(result.isSuccess)
        assertEquals("facebook-uid-3", result.getOrNull()?.username)
    }

    @Test
    fun invoke_failure_returnsRepositoryFailure() = runTest {
        val error = IllegalStateException("facebook sign-in failed")
        whenever(remoteAuthRepository.signInWithFacebook("access-token")).thenReturn(
            Result.failure(error)
        )

        val result = useCase("access-token")

        assertTrue(result.isFailure)
        assertSame(error, result.exceptionOrNull())
    }
}
