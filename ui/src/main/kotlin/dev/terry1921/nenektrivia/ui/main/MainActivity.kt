package dev.terry1921.nenektrivia.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.terry1921.nenektrivia.ui.navigation.AppNavHost
import dev.terry1921.nenektrivia.ui.preferences.PreferencesViewModel
import dev.terry1921.nenektrivia.ui.theme.NenekTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val startupViewModel: AppStartupViewModel = hiltViewModel()
            val isReady by startupViewModel.isReady.collectAsState()
            splashScreen.setKeepOnScreenCondition { !isReady }

            val vm: PreferencesViewModel = hiltViewModel()
            val settings by vm.uiState.collectAsState()
            NenekTheme(theme = settings.selectedTheme) {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
