package dev.terry1921.nenektrivia.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.terry1921.nenektrivia.ui.navigation.AppNavHost
import dev.terry1921.nenektrivia.ui.preferences.PreferencesViewModel
import dev.terry1921.nenektrivia.ui.theme.NenekTheme
import java.lang.Thread.sleep

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        setContent {
            val vm: PreferencesViewModel = hiltViewModel()
            val settings by vm.uiState.collectAsState()
            NenekTheme(theme = settings.selectedTheme) {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }

        sleep(500) // Simula que se está cargando algo
        splashScreen.setKeepOnScreenCondition { false }
    }
}
