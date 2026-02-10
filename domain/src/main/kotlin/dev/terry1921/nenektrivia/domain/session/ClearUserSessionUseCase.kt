package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.network.auth.RemoteAuthRepository
import javax.inject.Inject

class ClearUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val remoteAuthRepository: RemoteAuthRepository
) {
    suspend operator fun invoke() {
        remoteAuthRepository.signOut()
        authRepository.clearActiveSession()
    }
}
