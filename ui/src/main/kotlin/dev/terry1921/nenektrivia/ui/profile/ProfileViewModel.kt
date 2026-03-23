package dev.terry1921.nenektrivia.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.session.GetUserSessionUseCase
import dev.terry1921.nenektrivia.model.category.Category
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getUserSession: GetUserSessionUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        Timber.d("ProfileViewModel initialized")
        load()
    }

    fun load() = viewModelScope.launch {
        Timber.d("Loading profile")
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        runCatching {
            getUserSession()
        }.onSuccess { activeUser ->
            if (activeUser == null) {
                Timber.w("No active user session found while loading profile")
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    error = "No active user session found."
                )
            } else {
                Timber.i("Profile loaded for user: %s", activeUser.username)
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    displayName = activeUser.username,
                    avatarUrl = activeUser.photoUrl,
                    knowledge = linkedMapOf(
                        Category.Art to 68,
                        Category.Sports to 80,
                        Category.General to 45,
                        Category.Geography to 90,
                        Category.History to 12
                    )
                )
            }
        }.onFailure { throwable ->
            Timber.e(throwable, "Failed to load profile")
            _uiState.value = ProfileUiState(
                isLoading = false,
                error = throwable.message ?: "Error loading profile."
            )
        }
    }

    fun retry() {
        Timber.d("Profile retry requested")
        load()
    }
}
