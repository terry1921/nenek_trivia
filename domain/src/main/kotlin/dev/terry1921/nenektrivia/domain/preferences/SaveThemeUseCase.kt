package dev.terry1921.nenektrivia.domain.preferences

import dev.terry1921.nenektrivia.database.preferences.PreferencesRepository
import dev.terry1921.nenektrivia.model.category.Theme
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(private val repository: PreferencesRepository) {
    suspend operator fun invoke(theme: Theme) = repository.setTheme(theme)
}
