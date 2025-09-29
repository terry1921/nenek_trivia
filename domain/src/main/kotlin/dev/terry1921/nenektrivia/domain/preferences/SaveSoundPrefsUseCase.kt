package dev.terry1921.nenektrivia.domain.preferences

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import javax.inject.Inject

class SaveSoundPrefsUseCase @Inject constructor(private val repository: PreferencesRepository) {
    suspend fun setMusic(enabled: Boolean) = repository.setMusicEnabled(enabled)
    suspend fun setHaptics(enabled: Boolean) = repository.setHapticsEnabled(enabled)
}
