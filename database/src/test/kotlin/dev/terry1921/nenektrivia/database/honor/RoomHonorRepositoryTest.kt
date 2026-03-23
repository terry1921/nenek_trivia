package dev.terry1921.nenektrivia.database.honor

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.HonorDao
import dev.terry1921.nenektrivia.database.entity.Honor
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomHonorRepositoryTest {

    private lateinit var dao: HonorDao
    private lateinit var repository: RoomHonorRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = RoomHonorRepository(dao)
    }

    @Test
    fun `getById should call dao findById`() = runTest {
        val honor = createHonor()
        whenever(dao.findById(honor.id)).thenReturn(honor)

        val result = repository.getById(honor.id)

        verify(dao).findById(honor.id)
        assertThat(result).isEqualTo(honor)
    }

    @Test
    fun `upsert should call dao upsert`() = runTest {
        val honor = createHonor()

        repository.upsert(honor)

        verify(dao).upsert(honor)
    }

    private fun createHonor(
        id: String = "user-1",
        image: String? = "https://example.com/honor.png",
        points: Int = 100,
        username: String = "terry"
    ) = Honor(
        id = id,
        image = image,
        points = points,
        username = username
    )
}
