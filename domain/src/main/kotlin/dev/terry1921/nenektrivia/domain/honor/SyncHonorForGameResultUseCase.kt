package dev.terry1921.nenektrivia.domain.honor

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.entity.Honor
import dev.terry1921.nenektrivia.database.honor.HonorRepository as LocalHonorRepository
import dev.terry1921.nenektrivia.domain.leaderboard.LeaderboardRefreshSignal
import dev.terry1921.nenektrivia.model.honor.HonorModel
import dev.terry1921.nenektrivia.network.honor.HonorRepository as RemoteHonorRepository
import dev.terry1921.nenektrivia.network.leaderboard.LeaderboardRepository as RemoteLeaderboardRepository
import javax.inject.Inject

class SyncHonorForGameResultUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val localHonorRepository: LocalHonorRepository,
    private val remoteHonorRepository: RemoteHonorRepository,
    private val remoteLeaderboardRepository: RemoteLeaderboardRepository,
    private val leaderboardRefreshSignal: LeaderboardRefreshSignal
) {
    suspend operator fun invoke(points: Int) {
        val user = authRepository.getActiveUser() ?: return
        val existing = localHonorRepository.getById(user.id)
        val shouldSync = existing == null || points > existing.points
        if (!shouldSync) return

        val honor = Honor(
            id = user.id,
            image = user.photoUrl,
            points = points,
            username = user.username
        )
        authRepository.updateMaxPoints(user.id, points)
        localHonorRepository.upsert(honor)
        remoteHonorRepository.upsertHonor(honor.toModel())
        remoteLeaderboardRepository.fetchLeaderboard(forceRefresh = true)
        leaderboardRefreshSignal.notifyRefresh()
    }

    private fun Honor.toModel() = HonorModel(
        id = id,
        image = image,
        points = points,
        username = username
    )
}
