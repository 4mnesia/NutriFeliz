package com.example.nutriapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nutriapp.navigation.NavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isClicked: Boolean,
           user: String,
           onTheme: () -> Unit,
           navController: NavController?){
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryColor = MaterialTheme.colorScheme.primary
    val borderColor = MaterialTheme.colorScheme.secondary
    TopAppBar(
        title = {
            Column {
                Text(text = "NutriTrack", color = onPrimaryColor)
                Text(text = "Hola, $user", color = onBackgroundColor)
            }
        },
        actions = {
                IconButton(
                    onClick = onTheme
                ) {
                    Icon(
                        imageVector = if (isClicked) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                        contentDescription = "Color Changing",
                        tint = onPrimaryColor
                    )
                }
                IconButton(
                    onClick ={
                        navController?.navigate(NavItem.Settings.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings ,
                        contentDescription = "Color Changing",
                        tint = onPrimaryColor
                    )
                }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = primaryColor
        ),
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .drawBehind {
            val strokeWidth = 2.dp.toPx()
            drawLine(
                color = borderColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth
            )
        }

    )
}
