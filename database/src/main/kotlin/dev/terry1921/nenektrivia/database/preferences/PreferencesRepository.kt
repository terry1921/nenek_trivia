package dev.terry1921.nenektrivia.database.preferences

import dev.terry1921.nenektrivia.model.category.Theme
import dev.terry1921.nenektrivia.model.category.preference.UserSession
import dev.terry1921.nenektrivia.model.category.preference.UserSettings
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val settings: Flow<UserSettings>
    val session: Flow<UserSession?>
    suspend fun setMusicEnabled(enabled: Boolean)
    suspend fun setHapticsEnabled(enabled: Boolean)
    suspend fun setTheme(theme: Theme)
    suspend fun setSession(session: UserSession)
    suspend fun clearSession()
}
