package dev.terry1921.nenektrivia.domain.leaderboard

import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import dev.terry1921.nenektrivia.network.leaderboard.LeaderboardRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetLeaderboardUseCaseTest {

    private val repository: LeaderboardRepository = mock()
    private val useCase = GetLeaderboardUseCase(repository)

    @Test
    fun invoke_withoutForceRefresh_returnsRepositoryLeaderboard() = runTest {
        val leaderboard = listOf(
            PlayerScore(
                id = "user-1",
                image = "https://example.com/avatar.png",
                points = 120,
                username = "Player One"
            )
        )
        whenever(repository.fetchLeaderboard(forceRefresh = false)).thenReturn(leaderboard)

        val result = useCase()

        assertEquals(leaderboard, result)
        verify(repository).fetchLeaderboard(forceRefresh = false)
    }

    @Test
    fun invoke_withForceRefreshTrue_delegatesToRepositoryWithRefreshEnabled() = runTest {
        val leaderboard = listOf(
            PlayerScore(
                id = "user-2",
                image = null,
                points = 200,
                username = "Player Two"
            )
        )
        whenever(repository.fetchLeaderboard(forceRefresh = true)).thenReturn(leaderboard)

        val result = useCase(forceRefresh = true)

        assertEquals(leaderboard, result)
        verify(repository).fetchLeaderboard(forceRefresh = true)
    }
}
