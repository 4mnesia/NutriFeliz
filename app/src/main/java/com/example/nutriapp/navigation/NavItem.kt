package com.example.nutriapp.navigation

sealed class NavItem(val route: String) {
    object Login : NavItem("login")
    object Registration : NavItem("registration")
    object TransicionLogin : NavItem("transicion_login")
    object Home : NavItem("home")
    object Progress : NavItem("progress")
    object Profile : NavItem("profile")
    object Settings : NavItem("settings")
}