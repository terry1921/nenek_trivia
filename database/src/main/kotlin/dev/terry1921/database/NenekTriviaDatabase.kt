package dev.terry1921.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.terry1921.database.dao.CategoryDao
import dev.terry1921.database.dao.GameSessionDao
import dev.terry1921.database.dao.QuestionDao
import dev.terry1921.database.dao.ScoreDao
import dev.terry1921.database.dao.SessionQuestionDao
import dev.terry1921.database.dao.UserDao
import dev.terry1921.database.entity.Category
import dev.terry1921.database.entity.GameSession
import dev.terry1921.database.entity.Question
import dev.terry1921.database.entity.Score
import dev.terry1921.database.entity.SessionQuestionCrossRef
import dev.terry1921.database.entity.User

@Database(
    entities = [Category::class, Question::class, User::class, Score::class, GameSession::class, SessionQuestionCrossRef::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(RoomConverters::class)
abstract class NenekTriviaDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun questionDao(): QuestionDao

    abstract fun userDao(): UserDao

    abstract fun scoreDao(): ScoreDao

    abstract fun gameSessionDao(): GameSessionDao

    abstract fun sessionQuestionDao(): SessionQuestionDao
}
