package com.example.nutriapp.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val route: String) {
    object Login : NavItem("login")
    object Registration : NavItem("registration")
    object TransicionLogin : NavItem("transicion_login")
    object Home : NavItem("home")
    object Progress : NavItem("progress")
    object Profile : NavItem("profile")
    object Settings : NavItem("settings")
}