package dev.terry1921.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import dev.terry1921.database.entity.Category
import dev.terry1921.database.entity.Question

data class QuestionWithCategory(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id",
    )
    val category: Category,
)
