package dev.terry1921.nenektrivia.database.session

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.GameSessionDao
import dev.terry1921.nenektrivia.database.dao.SessionQuestionDao
import dev.terry1921.nenektrivia.database.entity.GameSession
import dev.terry1921.nenektrivia.database.entity.SessionQuestionCrossRef
import dev.terry1921.nenektrivia.database.entity.User
import dev.terry1921.nenektrivia.database.relations.GameSessionWithUser
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomSessionRepositoryTest {

    private lateinit var sessionDao: GameSessionDao
    private lateinit var sessionQuestionDao: SessionQuestionDao
    private lateinit var repository: RoomSessionRepository

    @Before
    fun setUp() {
        sessionDao = mock()
        sessionQuestionDao = mock()
        repository = RoomSessionRepository(sessionDao, sessionQuestionDao)
    }

    @Test
    fun `startSession should call sessionDao start`() = runTest {
        val session = createSession()

        repository.startSession(session)

        verify(sessionDao).start(session)
    }

    @Test
    fun `finishSession should call sessionDao finish`() = runTest {
        repository.finishSession(
            sessionId = "session-1",
            endedAt = 200L,
            points = 150,
            answered = 10,
            correct = 8
        )

        verify(sessionDao).finish("session-1", 200L, 150, 10, 8)
    }

    @Test
    fun `recentSessionsForUser should call sessionDao recentForUser`() = runTest {
        val expected = listOf(
            GameSessionWithUser(
                session = createSession(),
                user = createUser()
            )
        )
        whenever(sessionDao.recentForUser("user-1", 5)).thenReturn(expected)

        val result = repository.recentSessionsForUser("user-1", 5)

        verify(sessionDao).recentForUser("user-1", 5)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `upsertSessionQuestion should call sessionQuestionDao upsert`() = runTest {
        val ref = createSessionQuestionCrossRef()

        repository.upsertSessionQuestion(ref)

        verify(sessionQuestionDao).upsert(ref)
    }

    @Test
    fun `correctCount should call sessionQuestionDao correctCount`() = runTest {
        whenever(sessionQuestionDao.correctCount("session-1")).thenReturn(7)

        val result = repository.correctCount("session-1")

        verify(sessionQuestionDao).correctCount("session-1")
        assertThat(result).isEqualTo(7)
    }

    private fun createSession(
        id: String = "session-1",
        userId: String = "user-1",
        startedAt: Long = 100L,
        endedAt: Long? = null,
        totalPoints: Int = 0,
        questionsAnswered: Int = 0,
        correctAnswers: Int = 0
    ) = GameSession(
        id = id,
        userId = userId,
        startedAt = startedAt,
        endedAt = endedAt,
        totalPoints = totalPoints,
        questionsAnswered = questionsAnswered,
        correctAnswers = correctAnswers
    )

    private fun createSessionQuestionCrossRef(
        sessionId: String = "session-1",
        questionId: String = "question-1",
        answeredIndex: Int? = 2,
        isCorrect: Boolean? = true,
        answeredAt: Long? = 150L
    ) = SessionQuestionCrossRef(
        sessionId = sessionId,
        questionId = questionId,
        answeredIndex = answeredIndex,
        isCorrect = isCorrect,
        answeredAt = answeredAt
    )

    private fun createUser(
        id: String = "user-1",
        username: String = "terry",
        maxPoints: Int = 150,
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
