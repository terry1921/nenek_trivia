package dev.terry1921.nenektrivia.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LeaderboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState(isLoading = true))
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _uiState.value = LeaderboardUiState(isLoading = true)
        delay(400)
        val mock = listOf<PlayerScore>(
            PlayerScore(1, null, "Terry1921", 1500),
            PlayerScore(2, null, "JaneDoe", 1200),
            PlayerScore(3, null, "JohnSmith", 1100),
            PlayerScore(4, null, "AliceWonder", 1000),
            PlayerScore(5, null, "BobBuilder", 900),
            PlayerScore(6, null, "CharlieBrown", 800),
            PlayerScore(7, null, "DoraExplorer", 700),
            PlayerScore(8, null, "EveOnline", 600),
            PlayerScore(9, null, "FrankCastle", 500),
            PlayerScore(10, null, "GraceHopper", 400)
        )
        _uiState.value = LeaderboardUiState(isLoading = false, players = mock)
    }

    fun retry() = load()
}

data class LeaderboardUiState(
    val isLoading: Boolean = true,
    val players: List<PlayerScore> = emptyList(),
    val error: String? = null
)
