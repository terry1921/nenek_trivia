package dev.terry1921.nenektrivia.database.categories

import dev.terry1921.nenektrivia.database.dao.CategoryDao
import dev.terry1921.nenektrivia.database.entity.Category

class RoomCategoryRepository(private val dao: CategoryDao) : CategoryRepository {
    override suspend fun getAllCategories(): List<Category> = dao.getAll()
}
