package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.network.auth.RemoteAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ClearUserSessionUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val remoteAuthRepository: RemoteAuthRepository = mock()
    private val useCase = ClearUserSessionUseCase(authRepository, remoteAuthRepository)

    @Test
    fun invoke_callsSignOutAndClearActiveSession() = runTest {
        useCase()

        verify(remoteAuthRepository).signOut()
        verify(authRepository).clearActiveSession()
    }
}
