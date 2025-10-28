package com.example.nutriapp.ui.theme

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
import androidx.compose.ui.graphics.Color

enum class ColorProfile {
    PREDETERMINADO,
    ROSA
}

private val DarkColorScheme = darkColorScheme(
    primary = PurpuraDarkPrimary,
    onPrimary = PurpuraDarkOnPrimary,
    secondary = PurpuraDarkSecondary,
    onSecondary = PurpuraDarkOnBackground,
    tertiary = PurpuraDarkTertiary,
    onTertiary = PurpuraDarkOnPrimary,
    background = PurpuraDarkBackground,
    onBackground = PurpuraDarkOnBackground,
    surface = PurpuraDarkSurface,
    onSurface = PurpuraDarkOnSurface,
)

private val LightRosaColorScheme = lightColorScheme(
    primary = PredeterminadoLightPrimary,
    onPrimary = PredeterminadoLightOnPrimary,
    secondary = PredeterminadoLightSecondary,
    onSecondary = PredeterminadoLightOnSecondary,
    tertiary = PredeterminadoLightTertiary,
    onTertiary = PredeterminadoLightOnTertiary,
    background = PredeterminadoLightBackground,
    onBackground = PredeterminadoLightOnBackground,
    surface = PredeterminadoLightSurface,
    onSurface = PredeterminadoLightOnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = RosaClaroPrimary,       // Fondo azul para la TopBar
    secondary = RosaClaroSurface,
    tertiary = Color.White,            // Texto e iconos blancos para que contrasten
    background = RosaClaroBackground,
    surface = RosaClaroSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = RosaClaroOnBackground,
    onSurface = RosaClaroOnBackground
)

private val DarkRosaColorScheme = darkColorScheme(
    primary = RosaOscuroPrimary,      // Fondo rosa claro para la TopBar
    secondary = RosaOscuroSecondary,
    tertiary = RosaOscuroBackground,  // Texto e iconos oscuros para que contrasten
    background = RosaOscuroBackground,
    surface = RosaOscuroSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = RosaOscuroOnBackground,
    onSurface = RosaOscuroOnBackground
)

@Suppress("DEPRECATION")
@Composable
fun NutriAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorProfile: ColorProfile = ColorProfile.PREDETERMINADO,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // --- Selecciona esquema base según colorProfile ---
    val baseColorScheme = when (colorProfile) {
        ColorProfile.PREDETERMINADO -> if (darkTheme) DarkColorScheme else LightColorScheme
        ColorProfile.ROSA -> if (darkTheme) DarkRosaColorScheme else LightRosaColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    // --- Si se usan colores dinámicos (Android 12+) ---
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        baseColorScheme
    }

    // --- Configuración de la barra de estado ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    // --- Aplicar el tema final ---
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}



