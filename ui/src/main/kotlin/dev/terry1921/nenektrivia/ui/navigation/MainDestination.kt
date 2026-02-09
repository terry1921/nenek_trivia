package dev.terry1921.nenektrivia.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

sealed class MainDestination(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
) {
    data object Home : MainDestination(
        route = "home",
        label = "Inicio",
        icon = { Icon(Icons.Default.Home, contentDescription = null) }
    )

    data object Profile : MainDestination(
        route = "profile",
        label = "Mi Perfil",
        icon = { Icon(Icons.Default.Person, contentDescription = null) }
    )

    data object Leaderboard : MainDestination(
        route = "leaderboard",
        label = "Tabla",
        icon = { Icon(Icons.Default.List, contentDescription = null) }
    )

    data object Options : MainDestination(
        route = "options",
        label = "Opciones",
        icon = { Icon(Icons.Default.Settings, contentDescription = null) }
    )

    companion object {
        val items = listOf(Home, Profile, Leaderboard, Options)
    }
}
