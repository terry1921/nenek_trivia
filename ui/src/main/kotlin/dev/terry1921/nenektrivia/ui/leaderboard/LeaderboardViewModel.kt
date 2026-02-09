package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.leaderboard.GetLeaderboardUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState(isLoading = true))
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    init {
        load()
    }

    fun load(forceRefresh: Boolean = false) = viewModelScope.launch {
        _uiState.value = LeaderboardUiState(isLoading = true)
        _uiState.value = try {
            val players = getLeaderboardUseCase(forceRefresh = forceRefresh)
            LeaderboardUiState(isLoading = false, players = players)
        } catch (error: Exception) {
            if (error is kotlinx.coroutines.CancellationException) throw error
            LeaderboardUiState(
                isLoading = false,
                error = "No se pudo cargar el ranking."
            )
        }
    }

    fun retry() = load(forceRefresh = true)
}
