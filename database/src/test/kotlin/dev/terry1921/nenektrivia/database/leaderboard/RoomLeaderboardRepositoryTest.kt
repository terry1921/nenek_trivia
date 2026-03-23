package dev.terry1921.nenektrivia.database.leaderboard

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.ScoreDao
import dev.terry1921.nenektrivia.database.entity.Score
import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.database.relations.ScoreWithUser
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomLeaderboardRepositoryTest {

    private lateinit var dao: ScoreDao
    private lateinit var repository: RoomLeaderboardRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = RoomLeaderboardRepository(dao)
    }

    @Test
    fun `insertScore should call dao insert`() = runTest {
        val score = createScore()

        repository.insertScore(score)

        verify(dao).insert(score)
    }

    @Test
    fun `getTopScores should call dao topScores`() = runTest {
        val expected = listOf(
            ScoreWithUser(
                score = createScore(),
                user = createUser()
            )
        )
        whenever(dao.topScores(10)).thenReturn(expected)

        val result = repository.getTopScores(10)

        verify(dao).topScores(10)
        assertThat(result).isEqualTo(expected)
    }

    private fun createScore(
        id: String = "score-1",
        userId: String = "user-1",
        points: Int = 120,
        createdAt: Long = 123L
    ) = Score(
        id = id,
        userId = userId,
        points = points,
        createdAt = createdAt
    )

    private fun createUser(
        id: String = "user-1",
        username: String = "terry",
        maxPoints: Int = 120,
        photoUrl: String? = "https://example.com/avatar.png",
        isLoggedIn: Boolean = false
    ) = User(
        id = id,
        username = username,
        maxPoints = maxPoints,
        photoUrl = photoUrl,
        isLoggedIn = isLoggedIn
    )
}
