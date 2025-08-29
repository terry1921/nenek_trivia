package dev.terry1921.database.categories

import dev.terry1921.database.dao.CategoryDao
import dev.terry1921.database.entity.Category

class RoomCategoryRepository(
    private val dao: CategoryDao,
) : CategoryRepository {
    override suspend fun getAllCategories(): List<Category> = dao.getAll()
}
