package dev.terry1921.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import dev.terry1921.database.entity.GameSession
import dev.terry1921.database.entity.User

data class GameSessionWithUser(
    @Embedded val session: GameSession,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
    )
    val user: User,
)
