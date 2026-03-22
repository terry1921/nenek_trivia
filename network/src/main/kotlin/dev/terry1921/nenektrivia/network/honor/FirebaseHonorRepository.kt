package dev.terry1921.nenektrivia.network.honor

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.terry1921.nenektrivia.model.honor.HonorModel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseHonorRepository(
    private val database: FirebaseDatabase,
    private val cache: HonorCache,
    private val sessionCache: HonorSessionCache,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HonorRepository {

    override suspend fun fetchHonor(forceRefresh: Boolean): List<HonorModel> =
        withContext(dispatcher) {
            if (!forceRefresh) {
                sessionCache.read()?.let { return@withContext it }
                cache.readIfFresh(CACHE_TTL_MILLIS)?.let {
                    sessionCache.write(it)
                    return@withContext it
                }
            }

            val remote = fetchFromFirebase()
            cache.write(remote)
            sessionCache.write(remote)
            remote
        }

    override suspend fun upsertHonor(honor: HonorModel) = withContext(dispatcher) {
        suspendCancellableCoroutine { continuation ->
            val payload = mapOf(
                "id" to honor.id,
                "image" to honor.image,
                "points" to honor.points,
                "username" to honor.username
            )
            val reference = database.reference.child("honor").child(honor.id)
            reference.setValue(payload)
                .addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(Unit)
                    }
                }
                .addOnFailureListener { error ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(error)
                    }
                }
        }
        upsertCaches(honor)
    }

    private suspend fun fetchFromFirebase(): List<HonorModel> =
        suspendCancellableCoroutine { continuation ->
            val query = database.reference.child("honor").orderByChild("points")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val honor = snapshot.children.mapNotNull { child ->
                        val id = child.child("id").getValue(String::class.java)
                            ?: child.key
                            ?: return@mapNotNull null
                        val username = child.child("username").getValue(String::class.java)
                            ?: child.key
                            ?: return@mapNotNull null
                        val points = (child.child("points").value as? Number)?.toInt() ?: 0
                        val image = child.child("image").getValue(String::class.java)
                        HonorModel(
                            id = id,
                            image = image,
                            points = points,
                            username = username
                        )
                    }

                    val sorted = honor.sortedWith(
                        compareByDescending<HonorModel> { it.points }
                            .thenBy { it.username.lowercase() }
                    )
                    if (continuation.isActive) {
                        continuation.resume(sorted)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }

            query.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { query.removeEventListener(listener) }
        }

    private suspend fun upsertCaches(honor: HonorModel) {
        val cached = sessionCache.read()
            ?: cache.readIfFresh(CACHE_TTL_MILLIS)
            ?: emptyList()
        val updated = cached
            .filterNot { it.id == honor.id }
            .plus(honor)
            .sortedWith(
                compareByDescending<HonorModel> { it.points }
                    .thenBy { it.username.lowercase() }
            )
        cache.write(updated)
        sessionCache.write(updated)
    }

    private companion object {
        const val CACHE_TTL_MILLIS = 60 * 60 * 1000L
    }
}
