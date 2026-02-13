package dev.terry1921.nenektrivia.network.questions

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuestionsModule {

    @Provides
    @Singleton
    fun provideQuestionsRepository(database: FirebaseDatabase): QuestionsRepository =
        FirebaseQuestionsRepository(database)
}
