package com.example.nutriapp.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.navigation.NavigationApp
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal y punto de entrada de la interfaz de usuario de la aplicación.
 * Está anotada con `@AndroidEntryPoint` para habilitar la inyección de dependencias de Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Método del ciclo de vida que se llama cuando la actividad es creada.
     * Configura el contenido de la UI usando Jetpack Compose.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Habilita el modo de pantalla completa de borde a borde
        super.onCreate(savedInstanceState)
        setContent {
            // Controlador de navegación para gestionar las transiciones entre pantallas.
            val navController = rememberNavController()

            // Estado para gestionar el perfil de color actual de la app.
            var colorProfile by remember { mutableStateOf(ColorProfile.PREDETERMINADO) }

            // Determina si el sistema operativo está en modo oscuro.
            val darkTheme = isSystemInDarkTheme()

            // Aplica el tema principal de la aplicación.
            NutriAppTheme(
                darkTheme = darkTheme,
                colorProfile = colorProfile
            ) {
                // Composable principal que define la estructura de navegación de la app.
                NavigationApp(
                    navController = navController,
                    colorProfile = colorProfile,
                    setColorProfile = { nuevoPerfil -> colorProfile = nuevoPerfil },
                    darkTheme = darkTheme // <-- PARÁMETRO AÑADIDO
                )
            }
        }
    }
}
