package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.entity.User
import javax.inject.Inject

class SaveUserSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(user: User): Result<User> {
        val persistedUser = authRepository.getUserById(user.id)
            ?: run {
                val registerResult = authRepository.registerUser(user)
                if (registerResult.isFailure) {
                    authRepository.getUserById(user.id)
                        ?: authRepository.getUserByUsername(user.username)
                        ?: return Result.failure(
                            registerResult.exceptionOrNull()
                                ?: IllegalStateException("No se pudo registrar el usuario.")
                        )
                } else {
                    user
                }
            }

        authRepository.setActiveSession(persistedUser.id)
        return Result.success(persistedUser)
    }
}
