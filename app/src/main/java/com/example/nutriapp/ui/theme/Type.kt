package com.example.nutriapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nutriapp.R

/**
 * Define la familia de fuentes "Inter" cargando los archivos de fuente desde los recursos.
 * Esto permite usar la fuente personalizada en toda la aplicación.
 */
val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal), // Fuente para texto normal
    Font(R.font.inter_bold, FontWeight.Bold)      // Fuente para texto en negrita
)

/**
 * Define la escala tipográfica de la aplicación según las especificaciones de Material Design.
 * Aquí se configuran los estilos de texto para títulos, cuerpos, botones, etc.,
 * utilizando la familia de fuentes "Inter".
 */
val Typography = Typography(
    // Estilo por defecto para el cuerpo de texto principal.
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Estilo para títulos pequeños.
    headlineSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    /* Otros estilos de texto por defecto que puedes sobrescribir:
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
