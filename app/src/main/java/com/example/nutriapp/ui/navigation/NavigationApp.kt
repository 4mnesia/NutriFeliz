package com.example.nutriapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutriapp.ui.screen.HomeScreen
import com.example.nutriapp.ui.screen.LoginScreen
import com.example.nutriapp.ui.screen.RegistrationScreen
import com.example.nutriapp.ui.screen.TransicionLogin

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavItem.Login.route) {
        composable(NavItem.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(NavItem.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(
            route = NavItem.TransicionLogin.route + "/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Usuario"
            TransicionLogin(navController = navController, username = username)
        }
        composable(NavItem.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}
