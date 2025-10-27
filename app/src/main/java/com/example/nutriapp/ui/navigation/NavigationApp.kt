package com.example.nutriapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nutriapp.model.nav.NavIcon
import com.example.nutriapp.ui.screen.RegistrationScreen
import com.example.nutriapp.ui.screen.SettingsScreen
import com.example.nutriapp.ui.screen.TransicionLogin
import com.example.nutriapp.ui.screen.HomeScreen
import com.example.nutriapp.ui.screen.LoginScreen
import com.example.nutriapp.ui.theme.ColorProfile


@Composable
fun NavigationApp(
    navController: NavHostController,
    colorProfile: ColorProfile,
    setColorProfile: (ColorProfile) -> Unit
) {
    NavHost(
        navController = navController, 
        startDestination = NavItem.Login.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) }
    ) {
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
        composable(
            route = NavItem.Home.route + "/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Usuario"
            HomeScreen(navController = navController, username = username)
        }
        composable(NavItem.Settings.route) {
            SettingsScreen(
                navController = navController,
                colorProfile = colorProfile,
                setColorProfile = setColorProfile
            )
        }
    }
    val navItemList = listOf(
        NavIcon(label = "Inicio", Icons.Filled.Restaurant),
        NavIcon(label = "Progreso", Icons.AutoMirrored.Filled.TrendingUp),
        NavIcon(label = "Perfil", Icons.Filled.PersonOutline)
    )


}
