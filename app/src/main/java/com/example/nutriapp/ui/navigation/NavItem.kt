package com.example.nutriapp.ui.navigation

sealed class NavItem(val route: String) {
    object Login : NavItem("login")
    object Registration : NavItem("registration")
    object TransicionLogin : NavItem("transicion_login")
    object Home : NavItem("home")
}
