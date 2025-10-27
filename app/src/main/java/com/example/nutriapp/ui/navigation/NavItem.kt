package com.example.nutriapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val route: String) {
    object Login : NavItem("login")
    object Registration : NavItem("registration")
    object TransicionLogin : NavItem("transicion_login")
    object Home : NavItem("home")
    object Settings : NavItem("settings")
}
data class BottomNavItem(val label: String, val icon: ImageVector)
