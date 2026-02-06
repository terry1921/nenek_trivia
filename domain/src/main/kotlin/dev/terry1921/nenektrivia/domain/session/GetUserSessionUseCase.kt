package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import javax.inject.Inject

class GetUserSessionUseCase @Inject constructor(private val repository: PreferencesRepository) {
    operator fun invoke() = repository.session
}
