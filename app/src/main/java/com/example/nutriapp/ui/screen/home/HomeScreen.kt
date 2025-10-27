package com.example.nutriapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutriapp.ui.theme.home.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutriapp.ui.component.home.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (homeViewModel: HomeViewModel = viewModel()) {
    val uiState = homeViewModel.uiState
    NutriAppTheme(darkTheme = uiState.esTemaOscuro, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopBar(
                        isClicked = uiState.esTemaOscuro,
                        onTheme = { homeViewModel.onThemeChange() })
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




