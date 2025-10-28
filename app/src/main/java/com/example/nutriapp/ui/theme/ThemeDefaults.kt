package com.example.nutriapp.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object AppThemeDefaults {

    
    val defaultTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )


    val titleTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.primary
        )


    @Composable
    fun defaultButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    // Campos de texto
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