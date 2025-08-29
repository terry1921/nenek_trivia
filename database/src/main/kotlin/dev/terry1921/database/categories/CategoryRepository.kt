package dev.terry1921.database.categories

import dev.terry1921.database.entity.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
}
