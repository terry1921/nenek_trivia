package dev.terry1921.nenektrivia.domain.auth

import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.network.auth.RemoteAuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val remoteAuthRepository: RemoteAuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        remoteAuthRepository.signInWithGoogle(idToken).map { authUser ->
            val username = authUser.displayName?.takeIf { it.isNotBlank() }
                ?: authUser.email?.takeIf { it.isNotBlank() }
                ?: authUser.uid
            User(
                id = authUser.uid,
                username = username,
                photoUrl = authUser.photoUrl
            )
        }
}
