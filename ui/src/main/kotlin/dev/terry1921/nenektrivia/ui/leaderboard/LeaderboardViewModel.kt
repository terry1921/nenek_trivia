package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.leaderboard.GetLeaderboardUseCase
import dev.terry1921.nenektrivia.domain.leaderboard.LeaderboardRefreshSignal
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
    private val leaderboardRefreshSignal: LeaderboardRefreshSignal
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState(isLoading = true))
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    init {
        Timber.d("LeaderboardViewModel initialized")
        load()
        viewModelScope.launch {
            leaderboardRefreshSignal.events.collectLatest {
                Timber.d("Leaderboard refresh signal received")
                load(forceRefresh = true)
            }
        }
    }

    fun load(forceRefresh: Boolean = false) = viewModelScope.launch {
        Timber.d("Loading leaderboard. forceRefresh=%s", forceRefresh)
        _uiState.value = LeaderboardUiState(isLoading = true)
        _uiState.value = try {
            val players = getLeaderboardUseCase(forceRefresh = forceRefresh)
            Timber.i("Leaderboard loaded with %s players", players.size)
            LeaderboardUiState(isLoading = false, players = players)
        } catch (error: Exception) {
            if (error is kotlinx.coroutines.CancellationException) throw error
            Timber.e(error, "Failed to load leaderboard. forceRefresh=%s", forceRefresh)
            LeaderboardUiState(
                isLoading = false,
                error = "No se pudo cargar el ranking."
            )
        }
    }

    fun retry() {
        Timber.d("Leaderboard retry requested")
        load(forceRefresh = true)
    }
}
