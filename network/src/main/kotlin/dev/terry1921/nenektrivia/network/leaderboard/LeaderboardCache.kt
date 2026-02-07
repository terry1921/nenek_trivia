package dev.terry1921.nenektrivia.network.leaderboard

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import kotlinx.coroutines.flow.first

private val Context.leaderboardCacheDataStore by preferencesDataStore(name = "leaderboard_cache")

class LeaderboardCache(private val appContext: Context) {

    private object Keys {
        val DATA = stringPreferencesKey("leaderboard_data")
        val UPDATED_AT = longPreferencesKey("leaderboard_updated_at")
    }

    private val gson = Gson()
    private val type = object : TypeToken<List<PlayerScore>>() {}.type

    suspend fun readIfFresh(ttlMillis: Long): List<PlayerScore>? {
        val prefs = appContext.leaderboardCacheDataStore.data.first()
        val updatedAt = prefs[Keys.UPDATED_AT] ?: return null
        if (System.currentTimeMillis() - updatedAt > ttlMillis) return null
        val json = prefs[Keys.DATA] ?: return null
        return runCatching { gson.fromJson<List<PlayerScore>>(json, type) }.getOrNull()
    }

    suspend fun write(players: List<PlayerScore>) {
        val json = gson.toJson(players)
        appContext.leaderboardCacheDataStore.edit { prefs ->
            prefs[Keys.DATA] = json
            prefs[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
}
