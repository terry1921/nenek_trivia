package dev.terry1921.nenektrivia.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dev.terry1921.nenektrivia.ui.components.MainBottomBar
import dev.terry1921.nenektrivia.ui.navigation.MainDestination
import dev.terry1921.nenektrivia.ui.navigation.MainNavGraph
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onNavigatePrivacyPolicy: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            if (effect is MainEffect.NavigateToAuth) onLogoutClick()
        }
    }

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
            onLogoutClick = viewModel::onLogoutClick,
            onNavigatePrivacyPolicy = onNavigatePrivacyPolicy
        )
    }
}
