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

// -------------------- TEXTOS --------------------
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

// -------------------- BOTONES --------------------
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
