package com.example.nutriapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Componente de texto genérico para la aplicación.
 * Utiliza los estilos definidos en [AppThemeDefaults].
 *
 * @param text El texto a mostrar.
 * @param modifier Modificador de Compose.
 * @param style El estilo de texto a aplicar.
 */
@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppThemeDefaults.defaultTextStyle
) {
    Text(
        text = text,
        style = style,
        modifier = modifier
    )
}

/**
 * Componente para mostrar títulos en la aplicación.
 * Utiliza el estilo de título definido en [AppThemeDefaults].
 *
 * @param text El texto del título.
 * @param modifier Modificador de Compose.
 * @param style El estilo de texto a aplicar.
 */
@Composable
fun AppTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppThemeDefaults.titleTextStyle
) {
    Text(
        text = text,
        style = style,
        modifier = modifier
    )
}

/**
 * Botón genérico de la aplicación con los colores por defecto.
 *
 * @param onClick La acción a ejecutar cuando se presiona el botón.
 * @param text El texto que se muestra dentro del botón.
 * @param modifier Modificador de Compose.
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = AppThemeDefaults.defaultButtonColors(),
        modifier = modifier
    ) {
        Text(text)
    }
}

/**
 * Contenedor de tipo "Card" con los colores de superficie de la aplicación.
 * Ideal para agrupar contenido relacionado.
 *
 * @param modifier Modificador de Compose.
 * @param content El contenido a mostrar dentro de la tarjeta.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Contenedor de tipo "Box" con el color de fondo por defecto de la aplicación.
 *
 * @param modifier Modificador de Compose.
 * @param backgroundColor El color de fondo a aplicar.
 * @param content El contenido a mostrar dentro de la caja.
 */
@Composable
fun AppBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(backgroundColor),
        content = content
    )
}

/**
 * Menú desplegable genérico para la aplicación.
 *
 * @param T El tipo de los elementos en el menú.
 * @param expanded Estado que controla si el menú está abierto o cerrado.
 * @param onDismissRequest Acción a ejecutar cuando se solicita cerrar el menú.
 * @param items La lista de elementos a mostrar en el menú.
 * @param selectedItem El estado mutable que guarda el elemento actualmente seleccionado.
 * @param modifier Modificador de Compose.
 * @param itemLabel Función para obtener la etiqueta de texto de cada elemento.
 */
@Composable
fun <T> AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    selectedItem: MutableState<T>,
    modifier: Modifier = Modifier,
    itemLabel: (T) -> String = { it.toString() }
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { AppText(itemLabel(item)) },
                onClick = {
                    selectedItem.value = item
                    onDismissRequest()
                }
            )
        }
    }
}
