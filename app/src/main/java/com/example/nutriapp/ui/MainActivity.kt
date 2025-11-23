package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.navigation.NavigationApp
import com.example.nutriapp.repository.FoodRepository
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel
import com.example.nutriapp.viewmodel.home.HomeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }
            val darkTheme = isSystemInDarkTheme()

            // Instanciamos el repositorio y el ViewModel usando la Factory
            val repository = remember { FoodRepository() }
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )

            NutriAppTheme(
                darkTheme = darkTheme,
                colorProfile = colorProfile
            ) {
                NavigationApp(
                    navController = navController,
                    colorProfile = colorProfile,
                    setColorProfile = { nuevoPerfil -> colorProfile = nuevoPerfil },
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}