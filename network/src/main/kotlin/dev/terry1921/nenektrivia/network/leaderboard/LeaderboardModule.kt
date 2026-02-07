package dev.terry1921.nenektrivia.network.leaderboard

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database

    @Provides
    @Singleton
    fun provideLeaderboardCache(@ApplicationContext context: Context): LeaderboardCache =
        LeaderboardCache(context)

    @Provides
    @Singleton
    fun provideLeaderboardRepository(
        database: FirebaseDatabase,
        cache: LeaderboardCache
    ): LeaderboardRepository = FirebaseLeaderboardRepository(database, cache)
}
