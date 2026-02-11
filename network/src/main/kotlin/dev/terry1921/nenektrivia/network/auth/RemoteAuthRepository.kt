package dev.terry1921.nenektrivia.network.auth

interface RemoteAuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>

    suspend fun signInWithFacebook(accessToken: String): Result<AuthUser>

    suspend fun signOut()
}
