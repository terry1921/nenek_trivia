package dev.terry1921.nenektrivia.domain.session

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import dev.terry1921.nenektrivia.model.category.preference.UserSession
import javax.inject.Inject

class SaveUserSessionUseCase @Inject constructor(private val repository: PreferencesRepository) {
    suspend operator fun invoke(session: UserSession) = repository.setSession(session)
}
