package dev.terry1921.nenektrivia.network.honor

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HonorModule {

    @Provides
    @Singleton
    fun provideHonorCache(@ApplicationContext context: Context): HonorCache = HonorCache(context)

    @Provides
    @Singleton
    fun provideHonorRepository(
        database: FirebaseDatabase,
        cache: HonorCache,
        sessionCache: HonorSessionCache
    ): HonorRepository = FirebaseHonorRepository(database, cache, sessionCache)
}
