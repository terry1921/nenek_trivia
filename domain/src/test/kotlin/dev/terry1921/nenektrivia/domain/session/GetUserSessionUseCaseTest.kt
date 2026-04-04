package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.entity.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetUserSessionUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val useCase = GetUserSessionUseCase(authRepository)

    @Test
    fun invoke_returnsActiveUser_whenSessionExists() = runTest {
        val user = User(id = "1", username = "testuser")
        whenever(authRepository.getActiveUser()).thenReturn(user)

        val result = useCase()

        assertEquals(user, result)
    }

    @Test
    fun invoke_returnsNull_whenNoActiveSessionExists() = runTest {
        whenever(authRepository.getActiveUser()).thenReturn(null)

        val result = useCase()

        assertNull(result)
    }
}
