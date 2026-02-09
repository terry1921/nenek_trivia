package dev.terry1921.nenektrivia.model.auth

import kotlinx.coroutines.flow.Flow

/**
 * Service to handle authentication with different providers.
 */
interface AuthService {
    /**
     * Authenticates with Google using an ID token.
     */
    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    /**
     * Authenticates with Facebook using an access token.
     */
    suspend fun signInWithFacebook(accessToken: String): Result<Unit>

    /**
     * Signs out the current user.
     */
    fun signOut()

    /**
     * Returns a Flow that emits true if a user is logged in, false otherwise.
     */
    val isUserLoggedIn: Flow<Boolean>

    /**
     * Returns the current user's ID, or null if not logged in.
     */
    fun getCurrentUserId(): String?
}
