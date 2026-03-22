package dev.terry1921.nenektrivia.domain.honor

import dev.terry1921.nenektrivia.network.honor.HonorRepository
import javax.inject.Inject

class WarmHonorCacheUseCase @Inject constructor(private val honorRepository: HonorRepository) {
    suspend operator fun invoke() {
        runCatching {
            honorRepository.fetchHonor(forceRefresh = true)
        }.recoverCatching {
            honorRepository.fetchHonor(forceRefresh = false)
        }
    }
}
