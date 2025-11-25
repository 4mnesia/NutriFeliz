package com.example.nutriapp.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.example.nutriapp.viewmodel.home.HomeViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Composable que representa la pantalla de perfil del usuario.
 * Muestra la información del usuario, permite cambiar la foto de perfil, registrar el peso
 * y contiene una calculadora de macronutrientes.
 *
 * @param username El nombre del usuario a mostrar.
 * @param onLogout Callback que se ejecuta cuando el usuario cierra la sesión.
 * @param navController Controlador de navegación para manejar las transiciones.
 * @param homeViewModel ViewModel compartido que gestiona el estado y la lógica de negocio.
 */
@RequiresApi(Build.VERSION_CODES.O) // Anotación necesaria porque usamos APIs de java.time en el ViewModel.
@OptIn(ExperimentalMaterial3Api::class) // Anotación para usar componentes experimentales de Material 3.
@SuppressLint("DefaultLocale") // Suprime advertencias de formato de texto, ya que lo controlamos nosotros.
@Composable
fun ProfileScreen(
    username: String,
    onLogout: () -> Unit,
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    // --- ESTADOS DE LA UI ---
    // Cada `remember` crea una variable de estado que sobrevive a las recomposiciones de la UI.

    // Controla si la sección de la calculadora de macros es visible o no.
    var showDetails by remember { mutableStateOf(false) }
    
    // Estados para los campos de texto de la calculadora de macros.
    var weightForMacros by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Seleccionar sexo") }
    var activityLevel by remember { mutableStateOf("Seleccionar nivel") }
    var goal by remember { mutableStateOf("Seleccionar objetivo") }
    
    // Estados para almacenar los resultados de los cálculos.
    var imc by remember { mutableStateOf<Double?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var calculatedCalories by remember { mutableStateOf<Int?>(null) }
    var calculatedProteins by remember { mutableStateOf<Int?>(null) }
    var calculatedCarbs by remember { mutableStateOf<Int?>(null) }
    var calculatedFats by remember { mutableStateOf<Int?>(null) }

    // Estado para la foto de perfil seleccionada por el usuario.
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    // Controla la visibilidad del diálogo para elegir entre cámara o galería.
    var showDialog by remember { mutableStateOf(false) }
    // Estado para el campo de texto de registro de peso.
    var weightForChart by remember { mutableStateOf("") }

    // --- UTILIDADES DE COMPOSE ---

    // Estado para mostrar mensajes temporales (Snackbars).
    val snackbarHostState = remember { SnackbarHostState() }
    // CoroutineScope para lanzar operaciones asíncronas desde la UI (como mostrar un Snackbar).
    val scope = rememberCoroutineScope()
    // Contexto de la aplicación, necesario para permisos y acceso a archivos.
    val context = LocalContext.current
    // Gestor de foco para poder ocultar el teclado programáticamente.
    val focusManager = LocalFocusManager.current

    // Estado temporal para guardar la URI de la foto tomada con la cámara.
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- LANZADORES DE ACTIVIDADES (Activity Result Launchers) ---
    // Estos son los nuevos métodos en Compose para manejar los resultados de otras apps (galería, cámara, permisos).

    // Lanzador para abrir la galería y obtener una imagen.
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri // Cuando el usuario selecciona una imagen, actualizamos el estado.
    }

    // Lanzador para abrir la cámara y tomar una foto.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            imageUri = tempCameraUri // Si la foto se tomó con éxito, usamos la URI temporal guardada.
        }
    }
    
    // Función para crear un archivo temporal y lanzar la cámara.
    fun launchCamera() {
        val file = File.createTempFile(
            "profile_image_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        )
        // Se usa un FileProvider para compartir la URI del archivo de forma segura con la app de la cámara.
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        tempCameraUri = uri // Guardamos la URI antes de lanzar la cámara.
        cameraLauncher.launch(uri)
    }

    // Lanzador para solicitar el permiso de la cámara.
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchCamera() // Si el usuario concede el permiso, lanzamos la cámara.
        } else {
            // Si lo deniega, mostramos un mensaje informativo.
            scope.launch {
                snackbarHostState.showSnackbar("Permiso de cámara denegado.")
            }
        }
    }

    // --- ESTRUCTURA PRINCIPAL DE LA PANTALLA ---
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Lugar donde se mostrarán los Snackbars.
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus() // Oculta el teclado al tocar fuera de un campo de texto.
                }
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Permite hacer scroll en la pantalla si el contenido es muy largo.
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Diálogo para elegir entre cámara y galería.
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Seleccionar imagen") },
                    text = { Text("Elige una opción") },
                    confirmButton = {
                        Button(onClick = { galleryLauncher.launch("image/*"); showDialog = false }) {
                            Text("Galería")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { 
                            showDialog = false
                            // Comprueba si el permiso ya está concedido antes de lanzar la cámara.
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                launchCamera()
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }) {
                            Text("Cámara")
                        }
                    }
                )
            }

            // --- TARJETA DE PERFIL ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Muestra la imagen seleccionada o un icono por defecto.
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri), // Coil se encarga de cargar la imagen de forma asíncrona.
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(120.dp).clip(CircleShape).clickable { showDialog = true },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(120.dp).clickable { showDialog = true }
                        )
                    }
                    Text(username, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("$username@nutriapp.com", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { showDetails = !showDetails }) {
                        Text(if (showDetails) "Ocultar calculadora" else "Abrir calculadora de macros")
                    }
                }
            }

            // --- TARJETA DE REGISTRO DE PESO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Registra tu Peso", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Función local para evitar duplicar código en el TextField y el Button.
                    fun saveWeight() {
                        if (weightForChart.toFloatOrNull() != null) {
                            // Llama al ViewModel para guardar el peso en el backend.
                            homeViewModel.onSaveWeight(weightForChart)
                            scope.launch { snackbarHostState.showSnackbar("Peso guardado correctamente") }
                            weightForChart = "" // Limpia el campo de texto.
                            focusManager.clearFocus() // Oculta el teclado.
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Por favor, introduce un peso válido") }
                        }
                    }

                    OutlinedTextField(
                        value = weightForChart,
                        onValueChange = { weightForChart = it },
                        label = { Text("Peso actual (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { saveWeight() }), // Permite guardar al pulsar "Done" en el teclado.
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

            // --- TARJETA DE LA CALCULADORA DE MACROS ---
            // Solo se muestra si `showDetails` es true.
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

                        // Campos de entrada para los datos del usuario.
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(value = weightForMacros, onValueChange = { weightForMacros = it }, label = { Text("Peso (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Altura (cm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Edad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                            DropdownMenuSelector(options = listOf("Hombre", "Mujer"), selectedOption = gender, onOptionSelected = { gender = it }, label = "Sexo", modifier = Modifier.weight(1f))
                        }

                        DropdownMenuSelector(options = listOf("Sedentario", "Ligero", "Moderado", "Activo"), selectedOption = activityLevel, onOptionSelected = { activityLevel = it }, label = "Nivel de actividad")
                        DropdownMenuSelector(options = listOf("Bajar peso", "Mantener peso", "Subir masa muscular"), selectedOption = goal, onOptionSelected = { goal = it }, label = "Objetivo")

                        // Botón para ejecutar la lógica de cálculo.
                        Button(
                            onClick = {
                                val w = weightForMacros.toDoubleOrNull()
                                val h = height.toDoubleOrNull()
                                val a = age.toIntOrNull()

                                // Validación de todos los campos.
                                if (w == null || h == null || a == null || w <= 0 || h <= 0 || a <= 0 || gender == "Seleccionar sexo" || activityLevel == "Seleccionar nivel" || goal == "Seleccionar objetivo") {
                                    errorMessage = "Completa todos los campos."
                                    imc = null
                                    calculatedCalories = null
                                } else {
                                    errorMessage = null
                                    // Cálculo del IMC.
                                    val calculatedImc = w / ((h / 100) * (h / 100))
                                    imc = calculatedImc

                                    // Cálculo de la Tasa Metabólica Basal (TMB) con la fórmula de Harris-Benedict.
                                    val bmr = if (gender == "Hombre") {
                                        88.362 + (13.397 * w) + (4.799 * h) - (5.677 * a)
                                    } else { // Mujer
                                        447.593 + (9.247 * w) + (3.098 * h) - (4.330 * a)
                                    }

                                    // Multiplicador de actividad física.
                                    val activityMultiplier = when (activityLevel) {
                                        "Sedentario" -> 1.2
                                        "Ligero" -> 1.375
                                        "Moderado" -> 1.55
                                        "Activo" -> 1.725
                                        else -> 1.2
                                    }

                                    val maintenanceCalories = bmr * activityMultiplier

                                    // Ajuste de calorías según el objetivo.
                                    val finalCalories = when (goal) {
                                        "Bajar peso" -> maintenanceCalories - 500
                                        "Mantener peso" -> maintenanceCalories
                                        "Subir masa muscular" -> maintenanceCalories + 500
                                        else -> maintenanceCalories
                                    }
                                    calculatedCalories = finalCalories.toInt()

                                    // Distribución de macros (ejemplo: 40% carbos, 30% proteínas, 30% grasas).
                                    calculatedCarbs = (finalCalories * 0.40 / 4).toInt()
                                    calculatedProteins = (finalCalories * 0.30 / 4).toInt()
                                    calculatedFats = (finalCalories * 0.30 / 9).toInt()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Calcular Macros")
                        }

                        // --- SECCIÓN DE RESULTADOS ---
                        // Se muestra si hay un error o si los cálculos fueron exitosos.
                        if (errorMessage != null) {
                            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                        } else {
                            if (imc != null) {
                                Text("Tu IMC es: %.2f".format(imc), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                            }
                            if (calculatedCalories != null) {
                                Text("Calorías diarias: $calculatedCalories kcal", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                Text("Proteínas: $calculatedProteins g", fontWeight = FontWeight.Bold)
                                Text("Carbohidratos: $calculatedCarbs g", fontWeight = FontWeight.Bold)
                                Text("Grasas: $calculatedFats g", fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(16.dp))

                                // Botón para guardar las metas calculadas.
                                Button(
                                    onClick = {
                                        // Llama al ViewModel para guardar las metas en el backend.
                                        homeViewModel.onUpdateMacroGoals(
                                            calculatedCalories!!,
                                            calculatedProteins!!,
                                            calculatedCarbs!!,
                                            calculatedFats!!
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("¡Metas guardadas en tu perfil!")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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

/**
 * Un Composable reutilizable para mostrar un menú desplegable (Dropdown).
 */
@Composable
private fun DropdownMenuSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption.ifEmpty { label })
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NutriAppTheme {
        // La preview no funcionará correctamente con el ViewModel, por lo que se deja vacía o con datos de prueba.
        // ProfileScreen("Flavio", {}, rememberNavController(), hiltViewModel())
    }
}
