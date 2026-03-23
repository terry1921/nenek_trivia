package dev.terry1921.nenektrivia.database.categories

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.CategoryDao
import dev.terry1921.nenektrivia.database.entity.Category
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomCategoryRepositoryTest {

    private lateinit var dao: CategoryDao
    private lateinit var repository: RoomCategoryRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = RoomCategoryRepository(dao)
    }

    @Test
    fun `getAllCategories should call dao getAll`() = runTest {
        val expected = listOf(
            Category(id = 1, name = "Arte"),
            Category(id = 2, name = "Historia")
        )
        whenever(dao.getAll()).thenReturn(expected)

        val result = repository.getAllCategories()

        verify(dao).getAll()
        assertThat(result).isEqualTo(expected)
    }
}
