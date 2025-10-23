package com.example.nutriapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Paleta de colores para el tema oscuro de NutriApp
private val DarkColorScheme = darkColorScheme(
    primary = LightPurpleButton,       // Botones y elementos interactivos principales
    onPrimary = DarkPurpleBackground,  // Texto/iconos sobre elementos primarios
    secondary = LightPurpleText,       // Texto secundario, elementos menos importantes
    background = DarkPurpleBackground,   // Color de fondo de las pantallas
    surface = IntermediatePurple,      // Color de fondo de paneles, tarjetas
    onBackground = WhiteText,          // Texto sobre el fondo principal
    onSurface = WhiteText,             // Texto sobre paneles y tarjetas
)

@Composable
fun NutriAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography, // Corregido: Usamos el objeto Typography, no la familia de fuentes Inter
        content = content
    )
}
