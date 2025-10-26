package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.nutriapp.ui.navigation.NavigationApp
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(true) }
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

            NutriAppTheme(darkTheme = darkTheme, colorProfile = colorProfile) {
                NavigationApp(
                    toggleTheme = { darkTheme = !darkTheme },
                    colorProfile = colorProfile,
                    setColorProfile = { profile -> colorProfile = profile }
                )
            }
        }
    }
}
