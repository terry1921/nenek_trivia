package dev.terry1921.nenektrivia.network.auth

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.terry1921.nenektrivia.model.auth.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithFacebook(accessToken: String): Result<Unit> = try {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        firebaseAuth.signInWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override val isUserLoggedIn: Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid
}
