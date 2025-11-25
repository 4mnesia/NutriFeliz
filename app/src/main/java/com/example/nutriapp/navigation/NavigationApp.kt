package com.example.nutriapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.nutriapp.ui.component.BottomNavigationBar
import com.example.nutriapp.ui.component.TopBar
import com.example.nutriapp.ui.screen.*
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.LoginViewModel
import com.example.nutriapp.viewmodel.RegistrationViewModel
import com.example.nutriapp.viewmodel.home.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationApp(
    navController: NavHostController,
    colorProfile: ColorProfile,
    setColorProfile: (ColorProfile) -> Unit,
    darkTheme: Boolean // <-- PARÁMETRO AÑADIDO
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val username = navBackStackEntry?.arguments?.getString("username") ?: "Usuario"

    NutriAppTheme(darkTheme = darkTheme, colorProfile = colorProfile, dynamicColor = false) {
        val screensWithBottomBar = listOf(
            NavItem.Home.route,
            NavItem.Progress.route,
            NavItem.Profile.route,
        )
        val showBottomBar = currentRoute?.substringBefore("/") in screensWithBottomBar

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showBottomBar) {
                    TopBar(
                        user = username,
                        navController = navController
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController = navController, username = username)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = NavItem.Login.route,
                    enterTransition = { 
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500))
                    },
                    exitTransition = { 
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500))
                    }
                ) {
                    composable(NavItem.Login.route) {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(navController = navController, loginViewModel = loginViewModel)
                    }
                    composable(NavItem.Registration.route) {
                        val registrationViewModel: RegistrationViewModel = hiltViewModel()
                        RegistrationScreen(navController = navController, registrationViewModel = registrationViewModel)
                    }
                    composable(
                        route = NavItem.TransicionLogin.route + "/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        TransicionLogin(navController = navController, username = user)
                    }
                    composable(
                        route = NavItem.Home.route + "/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        // ¡Le pasamos los parámetros del tema a la HomeScreen!
                        HomeScreen(
                            navController = navController, 
                            username = user, 
                            homeViewModel = homeViewModel,
                            darkTheme = darkTheme,
                            colorProfile = colorProfile
                        )
                    }
                    composable(
                        route = NavItem.Progress.route + "/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        ProgressScreen(username = user, homeViewModel = homeViewModel)
                    }
                    composable(
                        route = NavItem.Profile.route + "/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        ProfileScreen(
                            username = user,
                            navController = navController,
                            homeViewModel = homeViewModel,
                            onLogout = {
                                navController.navigate(NavItem.Login.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(NavItem.Settings.route) {
                        SettingsScreen(
                            navController = navController,
                            colorProfile = colorProfile,
                            setColorProfile = setColorProfile,
                            onLogout = {
                                navController.navigate(NavItem.Login.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
