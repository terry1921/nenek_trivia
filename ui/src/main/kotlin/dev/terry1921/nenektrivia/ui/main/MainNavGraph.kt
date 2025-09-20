package dev.terry1921.nenektrivia.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.terry1921.nenektrivia.ui.home.HomeScreen
import dev.terry1921.nenektrivia.ui.leaderboard.LeaderboardScreen
import dev.terry1921.nenektrivia.ui.options.OptionsScreen
import dev.terry1921.nenektrivia.ui.profile.ProfileRoute
import dev.terry1921.nenektrivia.ui.profile.ProfileViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    onPlayClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainDestination.Home.route,
        modifier = modifier
    ) {
        composable(MainDestination.Home.route) {
            HomeScreen(
                title = "Nenek Trivia",
                subtitle = "Volando alto, jugando fuerte", // opcional, ref. a tu lema
                onPlayClick = onPlayClick
            )
        }
        composable(MainDestination.Profile.route) {
            val vm: ProfileViewModel = hiltViewModel()
            ProfileRoute(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainDestination.Leaderboard.route) { LeaderboardScreen() }
        composable(MainDestination.Options.route) { OptionsScreen(onLogoutClick) }
    }
}
