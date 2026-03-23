package dev.terry1921.nenektrivia.database.auth

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.UserDao
import dev.terry1921.nenektrivia.database.entity.User
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomAuthRepositoryTest {

    private lateinit var dao: UserDao
    private lateinit var repository: RoomAuthRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = RoomAuthRepository(dao)
    }

    @Test
    fun `registerUser should insert user and return success`() = runTest {
        val user = createUser()

        val result = repository.registerUser(user)

        verify(dao).insert(user)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `registerUser should return failure when dao insert throws`() = runTest {
        val user = createUser()
        val expected = IllegalStateException("insert failed")
        whenever(dao.insert(user)).doSuspendableAnswer { throw expected }

        val result = repository.registerUser(user)

        verify(dao).insert(user)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(expected)
    }

    @Test
    fun `getUserById should call dao findById`() = runTest {
        val user = createUser()
        whenever(dao.findById(user.id)).thenReturn(user)

        val result = repository.getUserById(user.id)

        verify(dao).findById(user.id)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `getUserByUsername should call dao findByUsername`() = runTest {
        val user = createUser()
        whenever(dao.findByUsername(user.username)).thenReturn(user)

        val result = repository.getUserByUsername(user.username)

        verify(dao).findByUsername(user.username)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `updateMaxPoints should call dao updateMaxPoints`() = runTest {
        val user = createUser()

        repository.updateMaxPoints(user.id, 42)

        verify(dao).updateMaxPoints(user.id, 42)
    }

    @Test
    fun `getActiveUser should call dao getActiveUser`() = runTest {
        val user = createUser(isLoggedIn = true)
        whenever(dao.getActiveUser()).thenReturn(user)

        val result = repository.getActiveUser()

        verify(dao).getActiveUser()
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `setActiveSession should clear previous session before setting new one`() = runTest {
        val userId = "user-1"

        repository.setActiveSession(userId)

        inOrder(dao) {
            verify(dao).clearActiveSession()
            verify(dao).setActiveSession(userId)
        }
    }

    @Test
    fun `clearActiveSession should call dao clearActiveSession`() = runTest {
        repository.clearActiveSession()

        verify(dao).clearActiveSession()
    }

    private fun createUser(
        id: String = "user-1",
        username: String = "terry",
        maxPoints: Int = 10,
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
