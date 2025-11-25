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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ColorProfile {
    PREDETERMINADO,
    ROSA
}

private val PredeterminadoColorScheme = darkColorScheme(
    primary = PurpleDarkPrimary,
    onPrimary = PurpleDarkOnPrimary,
    secondary = PurpleDarkSecondary,
    onSecondary = PurpleDarkOnBackground,
    tertiary = PurpleDarkTertiary,
    onTertiary = PurpleDarkOnPrimary,
    background = PurpleDarkBackground,
    onBackground = PurpleDarkOnBackground,
    surface = PurpleDarkSurface,
    onSurface = PurpleDarkOnSurface
)

private val RosaColorScheme = lightColorScheme(
    primary = PastelPinkPrimary,
    onPrimary = PastelPinkOnPrimary,
    secondary = PastelPinkSecondary,
    onSecondary = PastelPinkOnBackground,
    tertiary = PastelPinkTertiary,
    onTertiary = PastelPinkOnPrimary,
    background = PastelPinkBackground,
    onBackground = PastelPinkOnBackground,
    surface = PastelPinkSurface,
    onSurface = PastelPinkOnSurface
)

@Composable
fun NutriAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorProfile: ColorProfile = ColorProfile.PREDETERMINADO,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        colorProfile == ColorProfile.ROSA -> RosaColorScheme
        else -> PredeterminadoColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        // --- ¡LA SOLUCIÓN! ---
        // Este SideEffect ahora es seguro.
        SideEffect {
            // 1. Obtenemos el contexto de la vista.
            val currentContext = view.context
            // 2. Comprobamos si el contexto es una Activity. Esto fallará de forma segura en un Dialog.
            if (currentContext is Activity) {
                // 3. Solo si es una Activity, procedemos a cambiar la barra de estado.
                val window = currentContext.window
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
