package dev.terry1921.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * User profile
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)],
)
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "max_points") val maxPoints: Int = 0,
    @ColumnInfo(name = "photo_url") val photoUrl: String? = null,
)
