package dev.terry1921.nenektrivia.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { /* Handle result if needed */ }
    )
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
                },
                onNavigatePrivacyPolicy = {
                    // open chrome with url
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://nenek-trivia.web.app/privacy/")
                    ).also {
                        launcher.launch(it)
                    }
                }
            )
        }
        composable(Routes.MAIN) {
            MainScreen()
        }
    }
}
