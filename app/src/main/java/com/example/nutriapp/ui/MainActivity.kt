package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.ui.navigation.NavigationApp
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

            NutriAppTheme(
                colorProfile = colorProfile
            ) {
                NavigationApp(
                    navController = navController,
                    colorProfile = colorProfile,
                    setColorProfile = { nuevoPerfil -> colorProfile = nuevoPerfil }
                )
            }
        }
    }
}



