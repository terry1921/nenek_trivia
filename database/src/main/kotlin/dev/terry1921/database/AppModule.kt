package dev.terry1921.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Room Callback to seed data on first database creation.
     * We use a Provider<NenekTriviaDatabase> so we can get the DB instance inside the callback.
     */
    @Provides
    @Singleton
    fun provideRoomCallback(
        @ApplicationContext context: Context,
        dbProvider: Provider<NenekTriviaDatabase>,
    ): RoomDatabase.Callback =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed default categories asynchronously
                CoroutineScope(Dispatchers.IO).launch {
                    val database = dbProvider.get()
                    try {
                        database.categoryDao().upsertAll(CategorySeeds.default)
                    } catch (_: Throwable) {
                        // Seeding must never crash app start; swallow and log if needed
                    }
                }
            }
        }

    /**
     * Provide the Room database with migrations and callback.
     * Keep migrations array prepared for future versions.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        roomCallback: RoomDatabase.Callback,
    ): NenekTriviaDatabase {
        // If you add real migrations later, put them in this array and bump the @Database(version)
        val migrations: Array<out Migration> = emptyArray()

        return Room
            .databaseBuilder(
                context,
                NenekTriviaDatabase::class.java,
                BuildConfig.DATABASE_NAME,
            ).addCallback(roomCallback)
            .addMigrations(*migrations)
            .fallbackToDestructiveMigration() // Uncomment during early development only
            .build()
    }

    // MARK: - DAO providers

    @Provides
    fun provideCategoryDao(db: NenekTriviaDatabase) = db.categoryDao()

    @Provides
    fun provideQuestionDao(db: NenekTriviaDatabase) = db.questionDao()

    @Provides
    fun provideUserDao(db: NenekTriviaDatabase) = db.userDao()

    @Provides
    fun provideScoreDao(db: NenekTriviaDatabase) = db.scoreDao()

    @Provides
    fun provideGameSessionDao(db: NenekTriviaDatabase) = db.gameSessionDao()

    @Provides
    fun provideSessionQuestionDao(db: NenekTriviaDatabase) = db.sessionQuestionDao()
}
