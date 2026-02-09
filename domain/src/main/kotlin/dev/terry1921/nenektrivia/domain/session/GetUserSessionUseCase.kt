package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import javax.inject.Inject

class GetUserSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.getActiveUser()
}
