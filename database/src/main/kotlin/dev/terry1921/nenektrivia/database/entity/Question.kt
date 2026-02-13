package dev.terry1921.nenektrivia.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID
import kotlinx.parcelize.Parcelize

/**
 * Question payload persisted as received from Firebase `/questions`.
 */
@Entity(
    tableName = "questions",
    indices = [
        Index(value = ["category"]),
        Index(value = ["question_text"])
    ]
)
@Parcelize
data class Question(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "question_text") val question: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "answer_good") val answerGood: String,
    @ColumnInfo(name = "answer_bad_01") val answerBad01: String,
    @ColumnInfo(name = "answer_bad_02") val answerBad02: String,
    @ColumnInfo(name = "answer_bad_03") val answerBad03: String,
    @ColumnInfo(name = "tip") val tip: String? = null
) : Parcelable
