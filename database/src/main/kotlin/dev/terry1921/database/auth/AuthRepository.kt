package dev.terry1921.database.auth

import dev.terry1921.database.entity.User

interface AuthRepository {
    suspend fun registerUser(user: User): Result<Unit>

    suspend fun getUserByUsername(username: String): User?

    suspend fun updateMaxPoints(
        userId: String,
        points: Int,
    )
}
