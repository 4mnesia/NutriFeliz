package com.example.nutriapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nutriapp.model.home.Alimento
import com.example.nutriapp.ui.component.home.*
import com.example.nutriapp.ui.theme.ColorProfile
import com.example.nutriapp.ui.theme.NutriAppTheme
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
                query = uiState.foodQuery,
                quantity = uiState.foodQuantity,
                mealType = uiState.foodMealType,
                sugerencias = sugerencias,
                isButtonEnabled = uiState.isFoodFormValid,
                darkTheme = darkTheme,
                colorProfile = colorProfile,
                onDismiss = homeViewModel::onToggleFormularioComida,
                onQueryChange = homeViewModel::onFoodQueryChange,
                onQuantityChange = homeViewModel::onFoodQuantityChange,
                onMealTypeChange = homeViewModel::onFoodMealTypeChange,
                onSuggestionClicked = homeViewModel::onSuggestionClicked,
                onSaveClick = homeViewModel::onSaveCurrentFood
            )
        }
    }
}

@Composable
fun FormFood(
    query: String,
    quantity: String,
    mealType: String,
    sugerencias: List<Alimento>,
    isButtonEnabled: Boolean,
    darkTheme: Boolean,
    colorProfile: ColorProfile,
    onDismiss: () -> Unit,
    onQueryChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onMealTypeChange: (String) -> Unit,
    onSuggestionClicked: (Alimento) -> Unit,
    onSaveClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        NutriAppTheme(darkTheme = darkTheme, colorProfile = colorProfile) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    // --- ¡LA SOLUCIÓN! ---
                    // Se elimina el modificador `width(IntrinsicSize.Min)` que causaba el crash.
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Agregar Comida", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        label = { Text("¿Qué comiste?") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
                    )

                    if (sugerencias.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 150.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        ) {
                            items(sugerencias) { alimento ->
                                Text(
                                    text = alimento.nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSuggestionClicked(alimento) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                                Divider(color = MaterialTheme.colorScheme.background)
                            }
                        }
                    }

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = onQuantityChange,
                            label = { Text("Cantidad (g)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { if (isButtonEnabled) onSaveClick() })
                        )
                        Spacer(Modifier.width(8.dp))
                        MealTypeDropDown(selectedMeal = mealType, onMealSelected = onMealTypeChange, modifier = Modifier.weight(1f))
                    }

                    Row(Modifier.fillMaxWidth()) {
                        Button(
                            onClick = onSaveClick,
                            enabled = isButtonEnabled,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MealTypeDropDown(
    selectedMeal: String,
    onMealSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val mealOptions = listOf("Desayuno", "Almuerzo", "Cena", "Snacks")

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(selectedMeal)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.4f)
        ) {
            mealOptions.forEach { meal ->
                DropdownMenuItem(text = { Text(meal) }, onClick = {
                    onMealSelected(meal)
                    expanded = false
                })
            }
        }
    }
}
