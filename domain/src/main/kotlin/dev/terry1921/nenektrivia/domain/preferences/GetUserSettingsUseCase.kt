package dev.terry1921.nenektrivia.domain.preferences

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(private val repository: PreferencesRepository) {
    suspend operator fun invoke() = repository.settings
}
