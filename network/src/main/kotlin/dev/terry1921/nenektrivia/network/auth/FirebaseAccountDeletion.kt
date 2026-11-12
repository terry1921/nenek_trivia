package dev.terry1921.nenektrivia.network.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

enum class AccountProvider(val id: String) {
    GOOGLE(GoogleAuthProvider.PROVIDER_ID),
    FACEBOOK(FacebookAuthProvider.PROVIDER_ID)
}

class FirebaseAccountDeletion @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {
    val currentUid: String? get() = auth.currentUser?.uid

    fun providers(uid: String): Set<AccountProvider> {
        val user = checkNotNull(auth.currentUser?.takeIf { it.uid == uid })
        val providerIds = user.providerData.map { it.providerId }
        return AccountProvider.entries.filter { it.id in providerIds }.toSet()
    }

    suspend fun delete(
        uid: String,
        provider: AccountProvider,
        token: String,
        recordPendingLocalCleanup: () -> Unit
    ) {
        val user = checkNotNull(auth.currentUser?.takeIf { it.uid == uid })
        val credential = when (provider) {
            AccountProvider.GOOGLE -> GoogleAuthProvider.getCredential(token, null)
            AccountProvider.FACEBOOK -> FacebookAuthProvider.getCredential(token)
        }

        user.reauthenticate(credential).awaitCompletion()
        check(currentUid == uid)
        // Keep Firebase Auth intact until the server acknowledges deletion of the ranking entry.
        database.reference.child("honor").child(uid).removeValue().awaitCompletion()
        check(currentUid == uid)
        recordPendingLocalCleanup()
        user.delete().awaitCompletion()
    }

    private suspend fun Task<Void>.awaitCompletion() = suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(
                    task.exception
                        ?: IllegalStateException("Firebase did not complete the operation")
                )
            }
        }
    }
}
