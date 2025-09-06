package dev.terry1921.nenektrivia.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dev.terry1921.nenektrivia.ui.navigation.AppNavHost
import dev.terry1921.nenektrivia.ui.theme.NenekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NenekTheme(darkTheme = false) {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
