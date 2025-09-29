package dev.terry1921.nenektrivia.database.preferences

import dev.terry1921.nenektrivia.model.category.Theme
import dev.terry1921.nenektrivia.model.category.preference.UserSettings
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val settings: Flow<UserSettings>
    suspend fun setMusicEnabled(enabled: Boolean)
    suspend fun setHapticsEnabled(enabled: Boolean)
    suspend fun setTheme(theme: Theme)
}
