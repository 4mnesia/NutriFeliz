package com.example.nutriapp.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

/**
 * Objeto que centraliza los valores por defecto para los componentes personalizados de la aplicación.
 * Al obtener los estilos y colores directamente de [MaterialTheme], se asegura que los componentes
 * se adapten automáticamente al tema actual (claro, oscuro, etc.).
 */
object AppThemeDefaults {

    /**
     * Estilo de texto por defecto para los cuerpos de texto en la aplicación.
     */
    val defaultTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )

    /**
     * Estilo de texto por defecto para los títulos en la aplicación.
     */
    val titleTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.primary
        )

    /**
     * Colores por defecto para los botones principales de la aplicación.
     * @return Un objeto [ButtonColors] con la configuración de color.
     */
    @Composable
    fun defaultButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    /**
     * Colores por defecto para los campos de texto de la aplicación.
     * @return Un objeto `TextFieldColors` con la configuración de color.
     */
    @Composable
    fun defaultTextFieldColors() = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
    )
}
