package dev.terry1921.nenektrivia.network.honor

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.terry1921.nenektrivia.model.honor.HonorModel
import kotlinx.coroutines.flow.first

private val Context.honorCacheDataStore by preferencesDataStore(name = "honor_cache")

class HonorCache(private val appContext: Context) {

    private object Keys {
        val DATA = stringPreferencesKey("honor_data")
        val UPDATED_AT = longPreferencesKey("honor_updated_at")
    }

    private val gson = Gson()
    private val type = object : TypeToken<List<HonorModel>>() {}.type

    suspend fun readIfFresh(ttlMillis: Long): List<HonorModel>? {
        val prefs = appContext.honorCacheDataStore.data.first()
        val updatedAt = prefs[Keys.UPDATED_AT] ?: return null
        if (System.currentTimeMillis() - updatedAt > ttlMillis) return null
        val json = prefs[Keys.DATA] ?: return null
        return runCatching { gson.fromJson<List<HonorModel>>(json, type) }.getOrNull()
    }

    suspend fun write(items: List<HonorModel>) {
        val json = gson.toJson(items)
        appContext.honorCacheDataStore.edit { prefs ->
            prefs[Keys.DATA] = json
            prefs[Keys.UPDATED_AT] = System.currentTimeMillis()
        }
    }
}
