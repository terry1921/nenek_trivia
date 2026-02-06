package dev.terry1921.nenektrivia.database.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.terry1921.nenektrivia.model.category.Theme
import dev.terry1921.nenektrivia.model.category.preference.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "nenek_settings")

class DataStorePreferencesRepository(private val appContext: Context) : PreferencesRepository {

    private object Keys {
        val MUSIC = booleanPreferencesKey("music_enabled")
        val HAPTICS = booleanPreferencesKey("haptics_enabled")
        val THEME = stringPreferencesKey("theme_pref") // LIGHT/DARK/SYSTEM
    }

    override val settings: Flow<UserSettings> =
        appContext.dataStore.data.map { prefs ->
            UserSettings(
                isMusicEnabled = prefs[Keys.MUSIC] ?: true,
                isHapticsEnabled = prefs[Keys.HAPTICS] ?: true,
                theme = when (prefs[Keys.THEME]) {
                    Theme.LIGHT.name -> Theme.LIGHT
                    Theme.DARK.name -> Theme.DARK
                    else -> Theme.SYSTEM
                }
            )
        }

    override suspend fun setMusicEnabled(enabled: Boolean) {
        appContext.dataStore.edit { it[Keys.MUSIC] = enabled }
    }

    override suspend fun setHapticsEnabled(enabled: Boolean) {
        appContext.dataStore.edit { it[Keys.HAPTICS] = enabled }
    }

    override suspend fun setTheme(theme: Theme) {
        appContext.dataStore.edit { it[Keys.THEME] = theme.name }
    }
}
