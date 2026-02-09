package dev.terry1921.nenektrivia.network.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.terry1921.nenektrivia.model.auth.AuthService
import dev.terry1921.nenektrivia.network.auth.FirebaseAuthService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindAuthService(firebaseAuthService: FirebaseAuthService): AuthService

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
