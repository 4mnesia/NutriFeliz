package com.example.nutriapp.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.nutriapp.util.getTmpFileUri
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun ProfileScreen(
    username: String,
    onLogout: () -> Unit,
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    var showDetails by remember { mutableStateOf(false) }
    var weightForMacros by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Seleccionar sexo") }
    var activityLevel by remember { mutableStateOf("Seleccionar nivel") }
    var goal by remember { mutableStateOf("Seleccionar objetivo") }
    var imc by remember { mutableStateOf<Double?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var weightForChart by remember { mutableStateOf("") }

    var calculatedCalories by remember { mutableStateOf<Int?>(null) }
    var calculatedProteins by remember { mutableStateOf<Int?>(null) }
    var calculatedCarbs by remember { mutableStateOf<Int?>(null) }
    var calculatedFats by remember { mutableStateOf<Int?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = tempImageUri
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showDialog = true
            }
        }
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Seleccionar imagen") },
                    text = { Text("Elige una opción") },
                    confirmButton = {
                        Button(
                            onClick = {
                                galleryLauncher.launch("image/*")
                                showDialog = false
                            }
                        ) {
                            Text("Galería")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                getTmpFileUri(context).let { uri ->
                                    tempImageUri = uri
                                    cameraLauncher.launch(uri)
                                }
                                showDialog = false
                            }
                        ) {
                            Text("Cámara")
                        }
                    }
                )
            }

            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(120.dp)
                                .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) }
                        )
                    }
                    Text(username, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("$username@nutriapp.com", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onLogout() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar sesión")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { showDetails = !showDetails }) {
                        Text(if (showDetails) "Ocultar calculadora" else "Abrir calculadora de macros")
                    }
                }
            }

            // Weight registration card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Registra tu Peso", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    fun saveWeight() {
                        if (weightForChart.toFloatOrNull() != null) {
                            homeViewModel.onSaveWeight(weightForChart)
                            scope.launch {
                                snackbarHostState.showSnackbar("Peso guardado correctamente")
                            }
                            weightForChart = ""
                            focusManager.clearFocus()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor, introduce un peso válido")
                            }
                        }
                    }

                    OutlinedTextField(
                        value = weightForChart,
                        onValueChange = { weightForChart = it },
                        label = { Text("Peso actual (kg)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { saveWeight() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { saveWeight() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Peso")
                    }
                }
            }

            // Macro calculator card
            if (showDetails) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = weightForMacros,
                                onValueChange = { weightForMacros = it },
                                label = { Text("Peso (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                label = { Text("Altura (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Edad") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            DropdownMenuSelector(
                                options = listOf("Hombre", "Mujer"),
                                selectedOption = gender,
                                onOptionSelected = { gender = it },
                                label = "Sexo",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        DropdownMenuSelector(
                            options = listOf("Sedentario", "Ligero", "Moderado", "Activo"),
                            selectedOption = activityLevel,
                            onOptionSelected = { activityLevel = it },
                            label = "Nivel de actividad"
                        )

                        DropdownMenuSelector(
                            options = listOf("Bajar peso", "Mantener peso", "Subir masa muscular"),
                            selectedOption = goal,
                            onOptionSelected = { goal = it },
                            label = "Objetivo"
                        )

                        Button(
                            onClick = {
                                val w = weightForMacros.toDoubleOrNull()
                                val h = height.toDoubleOrNull()
                                val a = age.toIntOrNull()

                                if (w == null || h == null || a == null || w <= 0 || h <= 0 || a <= 0 || gender == "Seleccionar sexo" || activityLevel == "Seleccionar nivel" || goal == "Seleccionar objetivo") {
                                    errorMessage = "Completa todos los campos."
                                    imc = null
                                    calculatedCalories = null
                                } else {
                                    errorMessage = null
                                    val hMeters = h / 100
                                    imc = w / (hMeters * hMeters)

                                    val bmr = if (gender == "Hombre") 10 * w + 6.25 * h - 5 * a + 5 else 10 * w + 6.25 * h - 5 * a - 161
                                    val activityMultiplier = when (activityLevel) {
                                        "Sedentario" -> 1.2
                                        "Ligero" -> 1.375
                                        "Moderado" -> 1.55
                                        "Activo" -> 1.725
                                        else -> 1.2
                                    }
                                    val tdee = bmr * activityMultiplier
                                    val finalCalories = when (goal) {
                                        "Bajar peso" -> tdee - 500
                                        "Mantener peso" -> tdee
                                        "Subir masa muscular" -> tdee + 300
                                        else -> tdee
                                    }.toInt()

                                    val protein = (w * 2).toInt()
                                    val fat = w.toInt()
                                    val proteinCalories = protein * 4
                                    val fatCalories = fat * 9
                                    val carbs = ((finalCalories - proteinCalories - fatCalories) / 4).toInt()

                                    calculatedCalories = finalCalories
                                    calculatedProteins = protein
                                    calculatedCarbs = carbs
                                    calculatedFats = fat
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Calcular Macros / IMC")
                        }

                        errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }

                        imc?.let {
                            Text(
                                "Tu IMC es: ${String.format("%.2f", it)}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (calculatedCalories != null) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                Text("Metas calculadas:", style = MaterialTheme.typography.titleMedium)
                                Text("Calorías: $calculatedCalories kcal")
                                Text("Proteínas: $calculatedProteins g")
                                Text("Carbohidratos: $calculatedCarbs g")
                                Text("Grasas: $calculatedFats g")
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        homeViewModel.onUpdateMacroGoals(
                                            calculatedCalories!!,
                                            calculatedProteins!!,
                                            calculatedCarbs!!,
                                            calculatedFats!!
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Metas guardadas correctamente")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Guardar Metas")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NutriAppTheme {
        // ProfileScreen requires a HomeViewModel, so we can't easily preview it.
    }
}
