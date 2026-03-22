package dev.terry1921.nenektrivia.database.honor

import dev.terry1921.nenektrivia.database.dao.HonorDao
import dev.terry1921.nenektrivia.database.entity.Honor

class RoomHonorRepository(private val honorDao: HonorDao) : HonorRepository {
    override suspend fun getById(userId: String): Honor? = honorDao.findById(userId)

    override suspend fun upsert(honor: Honor) = honorDao.upsert(honor)
}
