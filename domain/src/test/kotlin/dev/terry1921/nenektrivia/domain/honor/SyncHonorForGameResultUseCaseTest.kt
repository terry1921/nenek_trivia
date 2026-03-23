package dev.terry1921.nenektrivia.domain.honor

import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.entity.Honor
import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.database.honor.HonorRepository as LocalHonorRepository
import dev.terry1921.nenektrivia.domain.leaderboard.LeaderboardRefreshSignal
import dev.terry1921.nenektrivia.model.honor.HonorModel
import dev.terry1921.nenektrivia.network.honor.HonorRepository as RemoteHonorRepository
import dev.terry1921.nenektrivia.network.leaderboard.LeaderboardRepository as RemoteLeaderboardRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class SyncHonorForGameResultUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val localHonorRepository: LocalHonorRepository = mock()
    private val remoteHonorRepository: RemoteHonorRepository = mock()
    private val remoteLeaderboardRepository: RemoteLeaderboardRepository = mock()
    private val leaderboardRefreshSignal: LeaderboardRefreshSignal = mock()
    private val useCase = SyncHonorForGameResultUseCase(
        authRepository = authRepository,
        localHonorRepository = localHonorRepository,
        remoteHonorRepository = remoteHonorRepository,
        remoteLeaderboardRepository = remoteLeaderboardRepository,
        leaderboardRefreshSignal = leaderboardRefreshSignal
    )

    @Test
    fun invoke_withoutActiveUser_doesNothing() = runTest {
        whenever(authRepository.getActiveUser()).thenReturn(null)

        useCase(points = 120)

        verify(authRepository).getActiveUser()
        verifyNoInteractions(
            localHonorRepository,
            remoteHonorRepository,
            remoteLeaderboardRepository
        )
        verify(authRepository, never()).updateMaxPoints(any(), any())
        verifyNoInteractions(leaderboardRefreshSignal)
    }

    @Test
    fun invoke_whenPointsDoNotBeatExistingHonor_doesNotSync() = runTest {
        val user = User(
            id = "user-1",
            username = "Player One",
            photoUrl = "https://example.com/avatar.png"
        )
        whenever(authRepository.getActiveUser()).thenReturn(user)
        whenever(localHonorRepository.getById("user-1")).thenReturn(
            Honor(
                id = "user-1",
                image = "https://example.com/avatar.png",
                points = 200,
                username = "Player One"
            )
        )

        useCase(points = 200)

        verify(localHonorRepository).getById("user-1")
        verify(authRepository, never()).updateMaxPoints(eq("user-1"), eq(200))
        verify(localHonorRepository, never()).upsert(org.mockito.kotlin.any())
        verify(remoteHonorRepository, never()).upsertHonor(org.mockito.kotlin.any())
        verify(remoteLeaderboardRepository, never()).fetchLeaderboard(forceRefresh = true)
        verifyNoInteractions(leaderboardRefreshSignal)
    }

    @Test
    fun invoke_whenPointsBeatExistingHonor_syncsLocalRemoteAndRefreshesLeaderboard() = runTest {
        val user = User(
            id = "user-2",
            username = "Player Two",
            photoUrl = "https://example.com/avatar.png"
        )
        whenever(authRepository.getActiveUser()).thenReturn(user)
        whenever(localHonorRepository.getById("user-2")).thenReturn(
            Honor(
                id = "user-2",
                image = null,
                points = 140,
                username = "Player Two"
            )
        )

        useCase(points = 180)

        val expectedHonor = Honor(
            id = "user-2",
            image = "https://example.com/avatar.png",
            points = 180,
            username = "Player Two"
        )
        verify(authRepository).updateMaxPoints("user-2", 180)
        verify(localHonorRepository).upsert(eq(expectedHonor))
        verify(remoteHonorRepository).upsertHonor(
            eq(
                HonorModel(
                    id = "user-2",
                    image = "https://example.com/avatar.png",
                    points = 180,
                    username = "Player Two"
                )
            )
        )
        verify(remoteLeaderboardRepository).fetchLeaderboard(forceRefresh = true)
        verify(leaderboardRefreshSignal).notifyRefresh()
    }
}
