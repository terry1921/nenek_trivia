package dev.terry1921.nenektrivia.database.categories

import dev.terry1921.nenektrivia.database.entity.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
}
