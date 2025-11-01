package com.example.nutriapp.navigation

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.nutriapp.ui.component.BottomNavigationBar
import com.example.nutriapp.ui.component.TopBar
import com.example.nutriapp.ui.screen.HomeScreen
import com.example.nutriapp.ui.screen.LoginScreen
import com.example.nutriapp.ui.screen.ProfileScreen
import com.example.nutriapp.ui.screen.ProgressScreen
import com.example.nutriapp.ui.screen.RegistrationScreen
import com.example.nutriapp.ui.screen.SettingsScreen
import com.example.nutriapp.ui.screen.TransicionLogin
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel

@Composable
fun NavigationApp(
    navController: NavHostController,
    colorProfile: ColorProfile,
    setColorProfile: (ColorProfile) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val username = navBackStackEntry?.arguments?.getString("username") ?: "Usuario"

    NutriAppTheme(darkTheme = colorProfile == ColorProfile.PREDETERMINADO, colorProfile = colorProfile, dynamicColor = false) {
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
                        val initialRoute = initialState.destination.route?.substringBefore("/")
                        val targetRoute = targetState.destination.route?.substringBefore("/")
                        val initialIndex = screensWithBottomBar.indexOf(initialRoute)
                        val targetIndex = screensWithBottomBar.indexOf(targetRoute)

                        if (initialIndex != -1 && targetIndex != -1) {
                            val direction = if (targetIndex > initialIndex) 1 else -1
                            slideInHorizontally(
                                initialOffsetX = { 1000 * direction },
                                animationSpec = tween(500)
                            )
                        } else {
                            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500))
                        }
                    },
                    exitTransition = {
                        val initialRoute = initialState.destination.route?.substringBefore("/")
                        val targetRoute = targetState.destination.route?.substringBefore("/")
                        val initialIndex = screensWithBottomBar.indexOf(initialRoute)
                        val targetIndex = screensWithBottomBar.indexOf(targetRoute)

                        if (initialIndex != -1 && targetIndex != -1) {
                            val direction = if (targetIndex > initialIndex) 1 else -1
                            slideOutHorizontally(
                                targetOffsetX = { -1000 * direction },
                                animationSpec = tween(500)
                            )
                        } else {
                            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500))
                        }
                    }
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
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        TransicionLogin(navController = navController, username = user)
                    }
                    composable(
                        route = NavItem.Home.route + "/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username") ?: "Usuario"
                        HomeScreen(navController = navController, username = user, homeViewModel = homeViewModel)
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
