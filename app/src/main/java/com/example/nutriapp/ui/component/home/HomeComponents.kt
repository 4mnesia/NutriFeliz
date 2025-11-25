package com.example.nutriapp.ui.component.home

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.R
import com.example.nutriapp.model.home.*


//Components
@Composable
fun MainBox(
    currentCalories: Int,
    goalCalories: Int,
    maxCalories: Int,
    progress: Float,
){
    val colorText = MaterialTheme.colorScheme.onBackground
    val colorText1 = MaterialTheme.colorScheme.tertiary
    Column(
        Modifier
            .size(width = 400.dp, height = 150.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 10.dp)
            )
    ){
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(size = 10.dp)
                ), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
            ) {

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image()
                    Column(modifier = Modifier
                        .padding(start = 8.dp)
                        .width(IntrinsicSize.Max)) {
                        MyText("Calorias Netas", colorText)
                        Row {

                            MyText("$currentCalories", color = colorText1, fontSize = 17.sp)
                            MyText("/$goalCalories", color = colorText1)
                        }
                        MyText("Máx: $maxCalories", colorText)
                    }
                    Spacer(Modifier.width(100.dp))
                    Column(modifier = Modifier
                        .padding(top = 1.dp)
                        .width(IntrinsicSize.Max)) {

                        MyText(
                            "${(progress * 100).toInt()}%",
                            colorText1,
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MyText("Objetivo", colorText, textAlign = TextAlign.Center)
                    }
                }
                Spacer(Modifier.height(40.dp))
                ColorChangingProgressBar(progress = progress)
            }
        }
    }
}


@Composable
fun BoxHome(
    proteinasActuales: Int,metaProteinas: Int,
    progresoProteinas: Float,
    carbosActuales: Int,
    metaCarbos: Int,
    progresoCarbos: Float,
    grasasActuales: Int,
    metaGrasas: Int,
    progresoGrasas: Float
) {
    Row(
        modifier = Modifier.size(width = 400.dp, height = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TargetBox(
            titulo = "Proteínas",
            icono = R.drawable.pollo,
            colorIcono = Color(0xFFff6467),
            colorFondoIcono = Color(0x2Fff6467),
            valorActual = proteinasActuales,
            valorMax = metaProteinas,
            progreso = progresoProteinas // ¡Le pasamos el progreso!
        )
        TargetBox(
            titulo = "Carbos",
            icono = R.drawable.hoja,
            colorIcono = Color(0xFF50A2FF),
            colorFondoIcono = Color(0x2F50A2FF),
            valorActual = carbosActuales,
            valorMax = metaCarbos,
            progreso = progresoCarbos // ¡Le pasamos el progreso!
        )
        TargetBox(
            titulo = "Grasas",
            icono = R.drawable.manzana,
            colorIcono = Color(0xFFFDC700),
            colorFondoIcono = Color(0x2FFDC700),
            valorActual = grasasActuales,
            valorMax = metaGrasas,
            progreso = progresoGrasas // ¡Le pasamos el progreso!
        )
    }
}

@Composable
fun ActionRegister(
    actividades: List<Actividad>,
    caloriasQuemadas: Int,
    formularioAbierto: Boolean,
    onToggleFormulario: () -> Unit,
    onActividadGuardada: (tipo: String, duracion: Int) -> Unit,
    onActividadBorrada: (actividad: Actividad) -> Unit
){
    //val colorText1 = MaterialTheme.colorScheme.tertiary
    //val colorText = MaterialTheme.colorScheme.onBackground
    Column(modifier = myBoxModifier(formularioAbierto )) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.padding(top=10.dp)){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.mancuerna),
                        "",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.background)
                    Column{
                        MyText(text = "Actividad Física")//, color = colorText1, fontSize = 17.sp)
                        MyText("$caloriasQuemadas calorías quemadas") //colorText)
                    }
                }
            }
            if (!formularioAbierto) {
                Butons(start = 90.dp,onClick = onToggleFormulario)
            }
        }
        Column(Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            FormACtivity(
                clicked = formularioAbierto,
                onGuardar = onActividadGuardada,
                onCancel = onToggleFormulario
            )
            if (!formularioAbierto) {
                if (actividades.isEmpty()) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MyText("No hay actividades registradas hoy")//, color = colorText1)
                    }
                } else {
                    LazyColumn(modifier = Modifier

                        .heightIn(max = 400.dp)) {
                        items(actividades, key = { it.id }) { actividad ->
                            ActividadGuardadaItem(
                                actividad = actividad,
                                onDelete = { onActividadBorrada(actividad)
                                }
                            )
                        }
                    }
                }
            }

        }

    }
}
@Composable
fun FoodRegister(
    listaComidas: List<ComidaAlacenada>,
    onAgregarClick: () -> Unit,
    onBorrarComida: (ComidaAlacenada) -> Unit
) {
    val colorText1 = MaterialTheme.colorScheme.tertiary
    Row(modifier = Modifier.fillMaxWidth()) {
        MyText(text = "Hoy", color = colorText1, fontSize = 17.sp)
        Butons(start = 250.dp,onClick = {onAgregarClick()})
    }
    if (listaComidas.isEmpty()) {
        Row (modifier = Modifier
            .size(width = 400.dp, height = 150.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 10.dp)
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier.width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(
                    "No has registrado comidas hoy",
                    color = colorText1,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                MyText(
                    "¡Comienza agregando tu primera comida!",
                    color = colorText1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }

        }
    }else {
        val comidasAlmacenadas = listaComidas.groupBy { it.tipoDeComida }
        LazyColumn(
            modifier = Modifier.heightIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            comidasAlmacenadas.forEach { (tipoComida, comidasDelTipo) ->

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        val icon = when (tipoComida) {
                            "Desayuno" -> Icons.Outlined.WbSunny
                            "Almuerzo" -> Icons.Outlined.Restaurant
                            "Cena" -> Icons.Outlined.Nightlight
                            else -> Icons.Outlined.Fastfood
                        }
                        MealTypeHeader(title = tipoComida, icon = icon)
                        comidasDelTipo.forEach { comida ->
                            FoodItemRow(comida = comida, onDelete = { onBorrarComida(comida) })
                            if (comida != comidasDelTipo.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("ModifierFactoryExtensionFunction")
@Composable
fun myBoxModifier(isExpanded: Boolean): Modifier {val commonModifier = Modifier
    .width(400.dp)
    .animateContentSize(animationSpec = tween(durationMillis = 800))
    .border(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(10.dp)
    )
    .background(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(size = 10.dp)
    )
    return if (isExpanded) {
        commonModifier.height(IntrinsicSize.Max)
    } else {
        commonModifier.heightIn(min = 150.dp)
    }
}

@Composable
fun RowScope.TargetBox(
    titulo: String,
    @DrawableRes icono: Int,
    colorIcono: Color,
    colorFondoIcono: Color,
    valorActual: Int,
    valorMax: Int,
    progreso: Float
){
    val colorText = MaterialTheme.colorScheme.onBackground
    val colorText1 = MaterialTheme.colorScheme.tertiary
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icono),
                contentDescription = titulo,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = colorFondoIcono, CircleShape),
                tint = colorIcono
            )
        }
        MyText(
            titulo,
            color = colorText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        MyText(
            "${valorActual}g",
            colorText1,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth()
        )
        MyText(
            "Máx: ${valorMax}g",
            color = colorText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row (Modifier.width(105.dp)){ ColorChangingProgressBar(progress = progreso) }

    }
}
//fin Boxes
@Composable
fun Image(){
    Image(
        painter = painterResource(R.drawable.large),
        contentDescription = "",
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f))
            .size(50.dp)
    )
}
// Formularios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormACtivity(clicked: Boolean,
                 onGuardar: (tipo: String, duracion: Int) -> Unit,
                 onCancel: () -> Unit
) {
    val ejercicios = listOf("Correr", "Caminar", "Nadar", "Bicicleta", "Gimnasio", "Deporte")
    var tipoEjercicio by remember { mutableStateOf(ejercicios[0]) }
    var expanded by remember { mutableStateOf(false) }
    var duracion by remember { mutableStateOf("30") }
    AnimatedVisibility(
        visible = clicked,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipoEjercicio,
                    onValueChange = {},
                    readOnly = true,
                    label = { if(!expanded)Text("Tipo de ejercicio") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ejercicios.forEach { seleccion ->
                        DropdownMenuItem(
                            text = { Text(seleccion) },
                            onClick = {
                                tipoEjercicio = seleccion
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración (minutos)") },
                enabled = clicked,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Muestra el teclado numérico
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            FormularioActionButtons(
                onGuardarClick ={
                    val duracionInt = duracion.toIntOrNull() ?: 0
                    if (duracionInt > 0) {
                        onGuardar(tipoEjercicio, duracionInt)
                    }
                },
                onCancelarClick = {
                    onCancel()
                }
            )
        }
    }
    Spacer(Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFood(
    onDismiss: () -> Unit,
    onGuardarComida: (alimento: Alimento, cantidad: Int, tipoComida: String) -> Unit,
    onBuscarComida: (String, Int, String) -> Unit,
    sugerencias: List<Alimento>,
    onQueryChange: (String) -> Unit
) {

    var textoDeBusqueda by remember { mutableStateOf("") }
    var alimentoSeleccionado by remember { mutableStateOf<Alimento?>(null) }
    var cantidad by remember { mutableStateOf("") }
    var nutrientesCalculados by remember { mutableStateOf(NutrientesCalculados()) }
    val tiposDeComida = listOf("Desayuno", "Almuerzo", "Cena", "Snack")
    var tipoComida by remember { mutableStateOf(tiposDeComida[0]) }
    var tipoComidaExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(alimentoSeleccionado, cantidad) {
        val cantidadInt = cantidad.toIntOrNull() ?: 0
        if (alimentoSeleccionado != null) {
            val ratio = cantidadInt / 100.0f
            nutrientesCalculados = NutrientesCalculados(
                calorias = (alimentoSeleccionado!!.caloriasPor100g * ratio).toInt(),
                proteinas = alimentoSeleccionado!!.proteinasPor100g * ratio,
                carbos = alimentoSeleccionado!!.carbosPor100g * ratio,
                grasas = alimentoSeleccionado!!.grasasPor100g * ratio
            )
        } else {
            nutrientesCalculados = NutrientesCalculados()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(350.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = textoDeBusqueda,
                onValueChange = {
                    textoDeBusqueda = it
                    alimentoSeleccionado = null
                    onQueryChange(it)
                },
                label = { Text("Buscar Alimento (API)") },
                placeholder = { Text("Ej: Pechuga de pollo...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (textoDeBusqueda.isNotBlank()) {
                            val cantidadInt = cantidad.toIntOrNull() ?: 100
                            onBuscarComida(textoDeBusqueda, cantidadInt, tipoComida)
                            onDismiss()
                        }
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        if (textoDeBusqueda.isNotBlank()) {
                            val cantidadInt = cantidad.toIntOrNull() ?: 100
                            onBuscarComida(textoDeBusqueda, cantidadInt, tipoComida)
                            onDismiss()
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar en API")
                    }
                }
            )

            if (sugerencias.isNotEmpty() && alimentoSeleccionado == null) {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                ) {
                    items(sugerencias) { alimento ->
                        ResultadoBusquedaItem(
                            alimento = alimento,
                            estaSeleccionado = false,
                            onClick = {
                                alimentoSeleccionado = alimento
                                textoDeBusqueda = alimento.nombre
                                onQueryChange("")
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }

            if (alimentoSeleccionado != null) {
                ResultadoBusquedaItem(
                    alimento = alimentoSeleccionado!!,
                    estaSeleccionado = true,
                    onClick = {
                        textoDeBusqueda = alimentoSeleccionado!!.nombre
                        alimentoSeleccionado = null
                    }
                )
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it.filter { char -> char.isDigit() } },
                label = { Text("Cantidad (gramos)") },
                placeholder = { Text("Ej: 100") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            if (alimentoSeleccionado != null && cantidad.isNotEmpty()) {
                ValoresNutricionalesCalculados(nutrientes = nutrientesCalculados)
            }
            ExposedDropdownMenuBox(
                expanded = tipoComidaExpanded,
                onExpandedChange = { tipoComidaExpanded = !tipoComidaExpanded }
            ) {
                OutlinedTextField(
                    value = tipoComida,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Comida") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoComidaExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = tipoComidaExpanded,
                    onDismissRequest = { tipoComidaExpanded = false }
                ) {
                    tiposDeComida.forEach { seleccion ->
                        DropdownMenuItem(
                            text = { Text(seleccion) },
                            onClick = {
                                tipoComida = seleccion
                                tipoComidaExpanded = false
                            }
                        )
                    }
                }
            }

            // Botón guardar normal para items seleccionados de la lista
            if (alimentoSeleccionado != null) {
                FormularioPrincipalButton(
                    texto = "Guardar Comida",
                    enabled = (cantidad.toIntOrNull() ?: 0) > 0,
                    onClick = {
                         onGuardarComida(alimentoSeleccionado!!, cantidad.toInt(), tipoComida)
                         onDismiss()
                    }
                )
            } else {
                // Si no hay seleccionado, permitimos buscar directamente en API
                val sePuedeBuscarAPI = textoDeBusqueda.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0
                 FormularioPrincipalButton(
                    texto = "Buscar y Guardar",
                    enabled = sePuedeBuscarAPI,
                    onClick = {
                         onBuscarComida(textoDeBusqueda, cantidad.toInt(), tipoComida)
                         onDismiss()
                    }
                )
            }
        }
    }
}




@Composable
fun ResultadoBusquedaItem(
    alimento: Alimento,
    estaSeleccionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            //.background(if (estaSeleccionado) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(alimento.nombre, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Text(
                text = "Por 100g: ${alimento.caloriasPor100g} cal • ${alimento.proteinasPor100g}g P • ${alimento.carbosPor100g}g C • ${alimento.grasasPor100g}g G",
                fontSize = 12.sp,
                //color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        if (estaSeleccionado) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Seleccionado",
                //tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ValoresNutricionalesCalculados(nutrientes: NutrientesCalculados) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Valores: ", style = MaterialTheme.typography.bodySmall)//, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${nutrientes.calorias} cal")
                }
                append(" • ${String.format("%.1f", nutrientes.proteinas)}g P")
                append(" • ${String.format("%.1f", nutrientes.carbos)}g C")
                append(" • ${String.format("%.1f", nutrientes.grasas)}g G")
            },
            //color = MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ActividadGuardadaItem(
    actividad: Actividad,
    onDelete: (Actividad) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.mancuerna),
                contentDescription = "Actividad física",
                //tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                MyText(
                    text = actividad.tipo,
                    //color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    MyText(
                        text = "${actividad.duracion} min",
                        //color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.large),
                        contentDescription = "Calorías",
                        //tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    MyText(
                        text = actividad.calorias.toString(),
                        //color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Icono de borrar
        IconButton(onClick = { onDelete(actividad) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Borrar actividad",
                //tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
@Composable
fun MealTypeHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            //tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, /*color = MaterialTheme.colorScheme.tertiary*/ fontWeight = FontWeight.Bold)
    }
}
@SuppressLint("DefaultLocale")
@Composable
fun FoodItemRow(
    comida: ComidaAlacenada,
    onDelete: () -> Unit
) {
    val nutrientes = remember(comida) {
        val ratio = comida.cantidadEnGramos / 100.0f
        NutrientesCalculados(
            calorias = (comida.alimento.caloriasPor100g * ratio).toInt(),
            proteinas = comida.alimento.proteinasPor100g * ratio,
            carbos = comida.alimento.carbosPor100g * ratio,
            grasas = comida.alimento.grasasPor100g * ratio
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${comida.alimento.nombre} (${comida.cantidadEnGramos}g)",
                //color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${nutrientes.calorias} cal")
                    }
                    append(" • ${String.format("%.1f", nutrientes.proteinas)}g P")
                    append(" • ${String.format("%.1f", nutrientes.carbos)}g C")
                    append(" • ${String.format("%.1f", nutrientes.grasas)}g G")
                },
                fontSize = 12.sp,
                //color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
        }
    }
}
