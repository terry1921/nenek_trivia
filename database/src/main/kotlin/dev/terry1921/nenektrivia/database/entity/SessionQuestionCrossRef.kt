package dev.terry1921.nenektrivia.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Cross‑refs (if you want to persist which questions were asked in a session)
 */
@Entity(
    tableName = "session_questions",
    primaryKeys = ["session_id", "question_id"],
    foreignKeys = [
        ForeignKey(
            entity = GameSession::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Question::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("question_id")]
)
data class SessionQuestionCrossRef(
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "question_id") val questionId: String,
    @ColumnInfo(name = "answered_index") val answeredIndex: Int? = null, // 0..3
    @ColumnInfo(name = "is_correct") val isCorrect: Boolean? = null,
    @ColumnInfo(name = "answered_at") val answeredAt: Long? = null
)
