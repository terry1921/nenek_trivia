package dev.terry1921.database.auth

import dev.terry1921.database.dao.UserDao
import dev.terry1921.database.entity.User

class RoomAuthRepository(
    private val dao: UserDao,
) : AuthRepository {
    override suspend fun registerUser(user: User): Result<Unit> =
        try {
            dao.insert(user)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }

    override suspend fun getUserByUsername(username: String): User? = dao.findByUsername(username)

    override suspend fun updateMaxPoints(
        userId: String,
        points: Int,
    ) = dao.updateMaxPoints(userId, points)
}
