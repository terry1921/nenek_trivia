package dev.terry1921.nenektrivia.network.leaderboard

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class FirebaseLeaderboardRepository(
    private val database: FirebaseDatabase,
    private val cache: LeaderboardCache,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LeaderboardRepository {

    override suspend fun fetchLeaderboard(forceRefresh: Boolean): List<PlayerScore> =
        withContext(dispatcher) {
            if (!forceRefresh) {
                cache.readIfFresh(CACHE_TTL_MILLIS)?.let { return@withContext it }
            }

            val remote = fetchFromFirebase()
            cache.write(remote)
            remote
        }

    private suspend fun fetchFromFirebase(): List<PlayerScore> =
        suspendCancellableCoroutine { continuation ->
            val query = database.reference.child("honor").orderByChild("points")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.children.mapNotNull { child ->
                        val id = child.child("id").getValue(String::class.java)
                            ?: child.key
                            ?: return@mapNotNull null
                        val username = child.child("username").getValue(String::class.java)
                            ?: child.key
                            ?: return@mapNotNull null
                        val points = (child.child("points").value as? Number)?.toInt() ?: 0
                        val image = child.child("image").getValue(String::class.java)
                        PlayerScore(
                            id = id,
                            image = image,
                            points = points,
                            username = username
                        )
                    }

                    val sorted = players.sortedWith(
                        compareByDescending<PlayerScore> { it.points }
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

    private companion object {
        const val CACHE_TTL_MILLIS = 60 * 60 * 1000L
    }
}
