package dev.terry1921.nenektrivia.domain.leaderboard

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LeaderboardRefreshSignalTest {

    @Test
    fun notifyRefresh_emitsEventToCollectors() = runTest {
        val signal = LeaderboardRefreshSignal()

        signal.events.test {
            signal.notifyRefresh()

            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun notifyRefresh_calledTwice_emitsTwoEvents() = runTest {
        val signal = LeaderboardRefreshSignal()

        signal.events.test {
            signal.notifyRefresh()
            signal.notifyRefresh()

            awaitItem()
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
