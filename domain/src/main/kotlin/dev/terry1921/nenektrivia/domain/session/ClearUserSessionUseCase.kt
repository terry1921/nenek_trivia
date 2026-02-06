package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import javax.inject.Inject

class ClearUserSessionUseCase @Inject constructor(private val repository: PreferencesRepository) {
    suspend operator fun invoke() = repository.clearSession()
}
