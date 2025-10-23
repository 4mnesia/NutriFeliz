package com.example.nutriapp.ui.navegation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nutriapp.ui.screen.HomeScreen
import com.example.nutriapp.ui.screen.ProfileScreen
import com.example.nutriapp.ui.screen.ProgressScreen
import com.example.nutriapp.ui.theme.IconColor
import com.example.nutriapp.ui.theme.NavBarBackground
import com.example.nutriapp.ui.theme.TextColor

@Composable
fun NavegacionApp (modifier: Modifier = Modifier) {

    val navItemList = listOf(
        NavItem(label = "Inicio", Icons.Filled.Restaurant),
        NavItem(label = "Progreso", Icons.AutoMirrored.Filled.TrendingUp),
        NavItem(label = "Perfil", Icons.Filled.PersonOutline),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = NavBarBackground) {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                    selectedIndex = index
                            },
                            icon = {
                                Icon(imageVector = navItem.icon, contentDescription ="Icon" )
                            },
                            label = {
                                Text(text = navItem.label)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IconColor,
                                unselectedIconColor = TextColor,
                                selectedTextColor = IconColor,
                                unselectedTextColor = TextColor,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex = selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int) {
    Box(modifier = modifier) {
        when(selectedIndex){
            0-> HomeScreen()
            1-> ProgressScreen()
            2-> ProfileScreen()
        }
    }
}
