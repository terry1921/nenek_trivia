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
import dev.terry1921.nenektrivia.ui.questions.QuestionScreen
import dev.terry1921.nenektrivia.ui.questions.QuestionsViewModel

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
                onPlayClick = { navController.navigate(QuestionDestination.ROUTE) }
            )
        }
        composable(QuestionDestination.ROUTE) {
            val vm: QuestionsViewModel = hiltViewModel()
            QuestionScreen(
                viewModel = vm,
                onBack = {
                    navController.navigate(MainDestination.Home.route) {
                        popUpTo(QuestionDestination.ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(MainDestination.Profile.route) {
            val vm: ProfileViewModel = hiltViewModel()
            ProfileRoute(viewModel = vm)
        }
        composable(MainDestination.Leaderboard.route) {
            val vm: LeaderboardViewModel = hiltViewModel()
            LeaderboardRoute(viewModel = vm)
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
