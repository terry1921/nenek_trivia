package dev.terry1921.nenektrivia.ui.navigation

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.terry1921.nenektrivia.ui.auth.AuthScreen
import dev.terry1921.nenektrivia.ui.auth.AuthViewModel
import dev.terry1921.nenektrivia.ui.main.MainScreen
import dev.terry1921.nenektrivia.ui.main.MainViewModel

const val PRIVACY_POLICY_URL = "https://nenek-trivia.web.app/privacy/"

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH,
        modifier = modifier
    ) {
        composable(Routes.AUTH) {
            val vm: AuthViewModel = hiltViewModel()
            AuthScreen(
                viewModel = vm,
                onNavigateMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigatePrivacyPolicy = {
                    val uri = PRIVACY_POLICY_URL.toUri()
                    CustomTabsIntent.Builder().build().launchUrl(context, uri)
                }
            )
        }
        composable(Routes.MAIN) {
            val vm: MainViewModel = hiltViewModel()
            MainScreen(
                viewModel = vm,
                onLogoutClick = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigatePrivacyPolicy = {
                    val uri = PRIVACY_POLICY_URL.toUri()
                    CustomTabsIntent.Builder().build().launchUrl(context, uri)
                }
            )
        }
    }
}
