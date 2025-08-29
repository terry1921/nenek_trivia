package dev.terry1921.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Question: exactly 1 correct + 3 incorrect options.
 * Enforce via app logic; DB holds 4 options and a correct index.
 */
@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("category_id"), Index(value = ["text"], unique = false)],
)
@Parcelize
data class Question(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "options") val options: List<String>, // size must be 4
    @ColumnInfo(name = "correct_index") val correctIndex: Int, // 0..3
    @ColumnInfo(name = "category_id") val categoryId: String,
) : Parcelable
