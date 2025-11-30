package com.example.nutriapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nutriapp.ui.component.home.*
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.viewmodel.home.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    username: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    darkTheme: Boolean,
    colorProfile: ColorProfile
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val sugerencias by homeViewModel.sugerencias.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainBox(uiState.caloriasNetas, uiState.metaCalorias, uiState.maxCalorias, uiState.progresoCalorias)
                BoxHome(uiState.proteinasConsumidas, uiState.metaProteinas, uiState.progresoProteinas, uiState.carbosConsumidos, uiState.metaCarbos, uiState.progresoCarbos, uiState.grasasConsumidas, uiState.metaGrasas, uiState.progresoGrasas)
                ActionRegister(uiState.listaActividades, uiState.caloriasQuemadas, uiState.formularioActividadAbierto, homeViewModel::onToggleFormularioActividad, homeViewModel::onGuardarActividad, homeViewModel::onBorrarActividad)
                FoodRegister(uiState.listaComidas, homeViewModel::onToggleFormularioComida, homeViewModel::onBorrarComida)
            }
        }

        if (uiState.formularioComidaAbierto) {
            FormFood(
                onDismiss = homeViewModel::onToggleFormularioComida,
                onGuardarComida = { alimento, cantidad, tipoComida ->
                    homeViewModel.onGuardarComida(alimento, cantidad, tipoComida)
                },
                onBuscarComida = { query, cantidad, tipoComida ->
                    homeViewModel.onFoodQueryChange(query)
                    homeViewModel.onFoodQuantityChange(cantidad.toString())
                    homeViewModel.onFoodMealTypeChange(tipoComida)
                    homeViewModel.onBuscarEnApi(query)
                },
                sugerencias = sugerencias,
                onQueryChange = homeViewModel::onFoodQueryChange
            )
        }
    }
}
