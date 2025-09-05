package dev.terry1921.nenektrivia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.terry1921.nenektrivia.ui.auth.AuthScreen
import dev.terry1921.nenektrivia.ui.auth.AuthViewModel
import dev.terry1921.nenektrivia.ui.main.MainScreen

object Routes {
    const val AUTH = "auth"
    const val MAIN = "main"
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH,
        modifier = modifier
    ) {
        composable(Routes.AUTH) {
            val vm: AuthViewModel = viewModel()
            AuthScreen(
                viewModel = vm,
                onNavigateMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.MAIN) {
            MainScreen()
        }
    }
}
