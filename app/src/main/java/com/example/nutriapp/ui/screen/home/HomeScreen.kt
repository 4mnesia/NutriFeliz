package com.example.nutriapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutriapp.ui.theme.home.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nutriapp.ui.component.home.*
import com.example.nutriapp.ui.component.home.TopBar
import com.example.nutriapp.ui.navigation.BottomNavItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    navController: NavController,
    username: String,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState = homeViewModel.uiState

    val navItems = listOf(
        BottomNavItem("Inicio", Icons.Default.Home),
        BottomNavItem("Progreso", Icons.AutoMirrored.Filled.TrendingUp),
        BottomNavItem("Perfil", Icons.Default.Person)
    )
    var selectedItem by remember { mutableStateOf(navItems.first()) }

    NutriAppTheme(darkTheme = uiState.esTemaOscuro, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopBar(
                        isClicked = uiState.esTemaOscuro,
                        onTheme = { homeViewModel.onThemeChange() })
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        navItems.forEach { item ->
                            NavigationBarItem(
                                selected = item == selectedItem,
                                onClick = { selectedItem = item },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                                    indicatorColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainBox(
                        currentCalories = uiState.caloriasNetas,
                        goalCalories = uiState.metaCalorias
                    )
                    BoxHome(
                        proteinasActuales = uiState.proteinasConsumidas,
                        metaProteinas = uiState.metaProteinas,
                        progresoProteinas = uiState.progresoProteinas,

                        carbosActuales = uiState.carbosConsumidos,
                        metaCarbos = uiState.metaCarbos,
                        progresoCarbos = uiState.progresoCarbos,

                        grasasActuales = uiState.grasasConsumidas,
                        metaGrasas = uiState.metaGrasas,
                        progresoGrasas = uiState.progresoGrasas
                    )
                    ActionRegister(
                        actividades = uiState.listaActividades,
                        caloriasQuemadas = uiState.caloriasQuemadas,
                        formularioAbierto = uiState.formularioActividadAbierto,
                        onToggleFormulario = { homeViewModel.onToggleFormularioActividad() },
                        onActividadGuardada = { tipo, duracion ->
                            homeViewModel.onGuardarActividad(
                                tipo,
                                duracion
                            )
                        },
                        onActividadBorrada = { actividad ->
                            homeViewModel.onBorrarActividad(
                                actividad
                            )
                        }
                    )

                    FoodRegister(onAgregarClick = { homeViewModel.onToggleFormularioComida() })
                }
            }

            if (uiState.formularioComidaAbierto) {
                FormFood(
                    onDismiss = { homeViewModel.onToggleFormularioComida() },
                    onGuardarComida = { alimento, cantidad,_->
                        homeViewModel.onGuardarComida(alimento, cantidad)
                    }
                )
            }
        }
    }
}




