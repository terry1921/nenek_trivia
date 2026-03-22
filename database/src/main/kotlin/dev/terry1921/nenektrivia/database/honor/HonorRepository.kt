package dev.terry1921.nenektrivia.database.honor

import dev.terry1921.nenektrivia.database.entity.Honor

interface HonorRepository {
    suspend fun getById(userId: String): Honor?

    suspend fun upsert(honor: Honor)
}
