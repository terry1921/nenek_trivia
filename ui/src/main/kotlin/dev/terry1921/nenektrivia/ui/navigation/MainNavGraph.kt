package dev.terry1921.nenektrivia.ui.navigation

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.terry1921.nenektrivia.ui.home.HomeRoute
import dev.terry1921.nenektrivia.ui.leaderboard.LeaderboardRoute
import dev.terry1921.nenektrivia.ui.leaderboard.LeaderboardViewModel
import dev.terry1921.nenektrivia.ui.preferences.PreferencesRoute
import dev.terry1921.nenektrivia.ui.preferences.PreferencesViewModel
import dev.terry1921.nenektrivia.ui.profile.ProfileRoute
import dev.terry1921.nenektrivia.ui.profile.ProfileViewModel

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onNavigatePrivacyPolicy: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainDestination.Home.route,
        modifier = modifier
    ) {
        composable(MainDestination.Home.route) {
            HomeRoute(
                onPlayClick = {} // TODO("Pendiente ir a pantalla de juego")
            )
        }
        composable(MainDestination.Profile.route) {
            val vm: ProfileViewModel = hiltViewModel()
            ProfileRoute(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainDestination.Leaderboard.route) {
            val vm: LeaderboardViewModel = hiltViewModel()
            LeaderboardRoute(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainDestination.Options.route) {
            val vm: PreferencesViewModel = hiltViewModel()
            val activity = LocalActivity.current as Activity
            PreferencesRoute(
                viewModel = vm,
                onLogout = onLogoutClick,
                onRate = { vm.startInAppReview(activity) },
                onPrivacy = onNavigatePrivacyPolicy
            )
        }
    }
}
