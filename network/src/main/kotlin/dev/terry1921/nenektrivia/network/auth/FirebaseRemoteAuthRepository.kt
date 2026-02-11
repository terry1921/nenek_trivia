package dev.terry1921.nenektrivia.network.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseRemoteAuthRepository(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteAuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
        signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))

    override suspend fun signInWithFacebook(accessToken: String): Result<AuthUser> =
        signInWithCredential(FacebookAuthProvider.getCredential(accessToken))

    private suspend fun signInWithCredential(credential: AuthCredential): Result<AuthUser> =
        withContext(dispatcher) {
            suspendCancellableCoroutine { continuation ->
                auth.signInWithCredential(credential)
                    .addOnSuccessListener { result ->
                        val user = result.user
                        if (user == null) {
                            if (continuation.isActive) {
                                continuation.resume(
                                    Result.failure(IllegalStateException("Usuario no disponible."))
                                )
                            }
                        } else if (continuation.isActive) {
                            continuation.resume(
                                Result.success(
                                    AuthUser(
                                        uid = user.uid,
                                        displayName = user.displayName,
                                        email = user.email,
                                        photoUrl = user.photoUrl?.toString()
                                    )
                                )
                            )
                        }
                    }
                    .addOnFailureListener { error ->
                        if (continuation.isActive) {
                            continuation.resume(Result.failure(error))
                        }
                    }
            }
        }

    override suspend fun signOut() {
        withContext(dispatcher) {
            auth.signOut()
        }
    }
}
