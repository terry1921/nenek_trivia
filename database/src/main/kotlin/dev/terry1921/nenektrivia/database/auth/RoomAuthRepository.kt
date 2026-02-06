package dev.terry1921.nenektrivia.database.auth

import dev.terry1921.nenektrivia.database.dao.UserDao
import dev.terry1921.nenektrivia.database.entity.User

class RoomAuthRepository(private val dao: UserDao) : AuthRepository {
    override suspend fun registerUser(user: User): Result<Unit> = try {
        dao.insert(user)
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(t)
    }

    override suspend fun getUserByUsername(username: String): User? = dao.findByUsername(username)

    override suspend fun updateMaxPoints(userId: String, points: Int) =
        dao.updateMaxPoints(userId, points)

    override suspend fun getActiveUser(): User? = dao.getActiveUser()

    override suspend fun setActiveSession(userId: String) {
        dao.clearActiveSession()
        dao.setActiveSession(userId)
    }

    override suspend fun clearActiveSession() = dao.clearActiveSession()
}
