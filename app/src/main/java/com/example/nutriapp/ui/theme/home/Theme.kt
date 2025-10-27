package com.example.nutriapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
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

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = Border,
    tertiary = white,
    onBackground = Purple40,
    background = Content1,
    surface = magent,
    onSurface = Buttons,
    onSecondary = formColor,
    onTertiary = barraStatuse,
    onTertiaryContainer= barraStatuse2
private val PredeterminadoLightColorScheme = lightColorScheme(
    primary = PredeterminadoClaroPrimary,
    secondary = PredeterminadoClaroSecondary,
    background = PredeterminadoClaroBackground,
    surface = PredeterminadoClaroSurface,
    onBackground = PredeterminadoClaroOnBackground,
    onSurface = PredeterminadoClaroOnBackground
)

private val LightColorScheme = lightColorScheme(
    primary = Border,
    secondary = Purple,
    tertiary = Purple40,
    onBackground = white,
    background = PurpleGrey80,
    surface = Content1,
    onSurface = PurpleClare,
    onSecondary = formColor2,
    onTertiary = barraStatuse2,
    onTertiaryContainer = barraStatuse
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
