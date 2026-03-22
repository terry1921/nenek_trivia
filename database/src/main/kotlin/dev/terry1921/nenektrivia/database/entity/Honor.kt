package dev.terry1921.nenektrivia.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Honor entries synced from Firebase `/honor`.
 */
@Entity(
    tableName = "honors",
    indices = [Index("points"), Index("username")]
)
data class Honor(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "image_url") val image: String? = null,
    @ColumnInfo(name = "points") val points: Int = 0,
    @ColumnInfo(name = "username") val username: String
)
