package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.ui.navigation.NavigationApp
import com.example.nutriapp.ui.theme.home.ColorProfile
import com.example.nutriapp.ui.theme.home.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var darkTheme by remember { mutableStateOf(true) }
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

            NutriAppTheme(
                darkTheme = darkTheme,
                colorProfile = colorProfile
            ) {
                NavigationApp(
                    navController = navController,
                    toggleTheme = { darkTheme = !darkTheme },
                    colorProfile = colorProfile,
                    setColorProfile = { nuevoPerfil -> colorProfile = nuevoPerfil }
                )
            }
        }
    }
}
@Preview(name = "App Main Preview", showBackground = true)
@Composable
private fun MainAppPreview(homeViewModel: HomeViewModel = viewModel()) {
    // 1. Simulamos la creación del estado que vive en MainActivity.
    //    Creamos un NavController falso y el estado para el tema.
    val navController = rememberNavController()
    var darkTheme by remember { mutableStateOf(true) }
    var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

    NutriAppTheme(
        darkTheme = darkTheme,
        colorProfile = colorProfile
    ) {

        NavigationApp(
            navController = navController,
            toggleTheme = { homeViewModel.onThemeChange() },
            colorProfile = colorProfile,
            setColorProfile = { newProfile -> colorProfile = newProfile }
        )
    }
}


