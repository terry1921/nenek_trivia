package dev.terry1921.nenektrivia.database.auth

import androidx.room.withTransaction
import dev.terry1921.nenektrivia.database.NenekTriviaDatabase
import javax.inject.Inject

class LocalAccountDeletion @Inject constructor(private val database: NenekTriviaDatabase) {
    suspend fun delete(uid: String) = database.withTransaction {
        database.honorDao().deleteById(uid)
        // Room cascades this deletion to scores, game sessions and their answers.
        database.userDao().deleteById(uid)
    }
}
