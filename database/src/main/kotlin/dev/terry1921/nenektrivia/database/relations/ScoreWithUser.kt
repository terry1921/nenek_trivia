package dev.terry1921.nenektrivia.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import dev.terry1921.nenektrivia.database.entity.Score
import dev.terry1921.nenektrivia.database.entity.User

data class ScoreWithUser(
    @Embedded val score: Score,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: User
)
