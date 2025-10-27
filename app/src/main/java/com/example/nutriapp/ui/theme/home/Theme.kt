package com.example.nutriapp.ui.theme.home

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ColorProfile {
    PREDETERMINADO,
    ROSA
}



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
    )

@Composable
fun NutriAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // CAMBIO 1: Añade el parámetro 'colorProfile'
    colorProfile: ColorProfile = ColorProfile.PREDETERMINADO,
    // (Opcional) Soporte para colores dinámicos de Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // CAMBIO 2: Usa una estructura 'when' para seleccionar la paleta de colores
    // correcta basándote en el 'colorProfile' y 'darkTheme'.
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> when (colorProfile) {
            ColorProfile.PREDETERMINADO -> DarkColorScheme
            // ColorProfile.OCEANO -> DarkOceanoColorScheme
            // ColorProfile.BOSQUE -> DarkBosqueColorScheme
            else -> DarkColorScheme
        }
        else -> when (colorProfile) {
            ColorProfile.PREDETERMINADO -> LightColorScheme
            // ColorProfile.OCEANO -> LightOceanoColorScheme
            // ColorProfile.BOSQUE -> LightBosqueColorScheme
            else -> LightColorScheme
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
