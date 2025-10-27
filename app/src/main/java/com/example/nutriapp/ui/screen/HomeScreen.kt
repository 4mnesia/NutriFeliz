package com.example.nutriapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutriapp.ui.component.home.*
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    navController: NavController,
    username: String,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
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
                    goalCalories = uiState.metaCalorias,
                    maxCalories = uiState.maxCalorias,
                    progress = uiState.progresoCalorias

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

                FoodRegister(
                    listaComidas = uiState.listaComidas,
                    onAgregarClick = { homeViewModel.onToggleFormularioComida() },
                    onBorrarComida = { comida -> homeViewModel.onBorrarComida(comida) }
                )
            }
        }

        if (uiState.formularioComidaAbierto) {
            FormFood(
                onDismiss = { homeViewModel.onToggleFormularioComida() },
                onGuardarComida = { alimento,cantidad,tipoComida ->
                    homeViewModel.onGuardarComida(alimento, cantidad, tipoComida)
                }
            )
        }
    }
}



@Preview(name = "Pantalla Principal", showBackground = true)
@Composable
private fun HomeScreenPreview() {
    NutriAppTheme {
        HomeScreen(
            navController = rememberNavController(),
            username = "Elmo"
        )
    }
}
