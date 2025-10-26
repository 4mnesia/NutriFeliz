package com.example.nutriapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class ColorProfile {
    PREDETERMINADO,
    ROSA
}

// --- Perfil Predeterminado ---
private val PredeterminadoDarkColorScheme = darkColorScheme(
    primary = PredeterminadoOscuroPrimary,
    secondary = PredeterminadoOscuroSecondary,
    background = PredeterminadoOscuroBackground,
    surface = PredeterminadoOscuroSurface,
    onBackground = PredeterminadoOscuroOnBackground,
    onSurface = PredeterminadoOscuroOnBackground
)

private val PredeterminadoLightColorScheme = lightColorScheme(
    primary = PredeterminadoClaroPrimary,
    secondary = PredeterminadoClaroSecondary,
    background = PredeterminadoClaroBackground,
    surface = PredeterminadoClaroSurface,
    onBackground = PredeterminadoClaroOnBackground,
    onSurface = PredeterminadoClaroOnBackground
)

// --- Perfil Rosa ---
private val RosaDarkColorScheme = darkColorScheme(
    primary = RosaOscuroPrimary,
    secondary = RosaOscuroSecondary,
    background = RosaOscuroBackground,
    surface = RosaOscuroSurface,
    onBackground = RosaOscuroOnBackground,
    onSurface = RosaOscuroOnBackground
)

private val RosaLightColorScheme = lightColorScheme(
    primary = RosaClaroPrimary,
    background = RosaClaroBackground,
    surface = RosaClaroSurface,
    onBackground = RosaClaroOnBackground,
    onSurface = RosaClaroOnBackground
)

@Composable
fun NutriAppTheme(
    darkTheme: Boolean = true,
    colorProfile: ColorProfile = ColorProfile.PREDETERMINADO,
    content: @Composable () -> Unit
) {
    val colorScheme = when (colorProfile) {
        ColorProfile.PREDETERMINADO -> if (darkTheme) PredeterminadoDarkColorScheme else PredeterminadoLightColorScheme
        ColorProfile.ROSA -> if (darkTheme) RosaDarkColorScheme else RosaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
