package dev.terry1921.nenektrivia.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onPlayClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                navController = navController,
                items = MainDestination.items
            )
        }
    ) { paddingValues ->
        MainNavGraph(
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            navController = navController,
            onPlayClick = onPlayClick,
            onLogoutClick = onLogoutClick
        )
    }
}
