package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.ui.navigation.NavigationApp

import com.example.nutriapp.ui.theme.home.NutriAppTheme
import com.example.nutriapp.ui.theme.home.ColorProfile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent es el único punto de entrada para toda tu UI de Compose.
        setContent {
            var navController = rememberNavController()
            var darkTheme by remember { mutableStateOf(true) }
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

            NutriAppTheme(darkTheme = darkTheme,
                colorProfile = colorProfile) {
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
