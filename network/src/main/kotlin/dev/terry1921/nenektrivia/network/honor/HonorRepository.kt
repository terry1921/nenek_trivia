package dev.terry1921.nenektrivia.network.honor

import dev.terry1921.nenektrivia.model.honor.HonorModel

interface HonorRepository {
    suspend fun fetchHonor(forceRefresh: Boolean = false): List<HonorModel>

    suspend fun upsertHonor(honor: HonorModel)
}
