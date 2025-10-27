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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.R
import com.example.nutriapp.model.home.*



//Components
@Composable
fun MainBox(   currentCalories: Int = 0,
               goalCalories: Int = 2000,
               maxCalories: Int = 2200
){
    val colorText = MaterialTheme.colorScheme.onBackground
    val colorText1 = MaterialTheme.colorScheme.tertiary
    val progress by remember { mutableFloatStateOf(currentCalories.toFloat() / goalCalories.toFloat()) }

    Column(Modifier
        .size(width = 400.dp, height = 150.dp)
        .border(
            border = BorderStroke(1.dp, Color(0xFF7e2a0c)),
            shape = RoundedCornerShape(10.dp)
        )
        .background(
            color = Color(0xAD1A0F2E),
            shape = RoundedCornerShape(size = 10.dp)
        )
    ){
        Column(
            modifier = Modifier
                .background(
                    color = Color(color = 0x227E2A0C),
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
    val colorText1 = MaterialTheme.colorScheme.tertiary
    val colorText = MaterialTheme.colorScheme.onBackground
    Column(modifier = myBoxModifier(formularioAbierto )) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.padding(top=10.dp)){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.mancuerna),
                        "",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.surface)
                    Column{
                        MyText(text = "Actividad Física", color = colorText1, fontSize = 17.sp)
                        MyText("$caloriasQuemadas calorías quemadas", colorText)
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MyText("No hay actividades registradas hoy", color = colorText1)
                    }
                } else {
                    LazyColumn {
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
fun FoodRegister(onAgregarClick: () -> Unit) {
    val colorText1 = MaterialTheme.colorScheme.tertiary
    Row(modifier = Modifier.fillMaxWidth()) {
        MyText(text = "Hoy", color = colorText1, fontSize = 17.sp)
        Butons(start = 250.dp,onClick = {onAgregarClick()})
    }
    //Ultimo target
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
        verticalAlignment = Alignment.CenterVertically){
        Column (Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally){
            MyText("No has registrado comidas hoy", color = colorText1, textAlign=TextAlign.Center, fontSize = 17.sp, modifier = Modifier.fillMaxWidth())
            MyText("¡Comienza agregando tu primera comida!", color = colorText1, textAlign=TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp))
        }
    }
}

@SuppressLint("ModifierFactoryExtensionFunction")
@Composable
fun myBoxModifier(onClick: Boolean): Modifier {
    if (!onClick) {
        return Modifier
            .width(400.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 800))
            .height(150.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 10.dp)
            )
    }
    return Modifier
        .width(400.dp)
        .animateContentSize(animationSpec = tween(durationMillis = 800))
        .height(IntrinsicSize.Max)
        .border(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(10.dp)
        )
        .background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(size = 10.dp)
        )
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
            .background(color = Color(0xFFff6900))
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
    val ejercicios = listOf("Correr", "Caminar", "Nadar", "Bicicleta", "Gimnasio")
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
                    color = MaterialTheme.colorScheme.onSecondary,
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
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color(0xFFD1C4E9),
                        focusedLabelColor = Color(0xFFD1C4E9),
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTrailingIconColor = Color(0xFFBDBDBD),
                        focusedTrailingIconColor = Color(0xFFBDBDBD),
                        unfocusedBorderColor = Color(0xFFA78BFA),
                        focusedBorderColor = Color(0xFFA78BFA)

                    )
                    ,
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryEditable,
                            enabled = true
                        )
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
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color(0xFFD1C4E9),
                    focusedLabelColor = Color(0xFFD1C4E9),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTrailingIconColor = Color(0xFFBDBDBD),
                    focusedTrailingIconColor = Color(0xFFBDBDBD),
                    unfocusedBorderColor = Color(0xFFA78BFA),
                    focusedBorderColor = Color(0xFFA78BFA)
                )
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
    onGuardarComida: (alimento: Alimento, cantidad: Int, tipoComida: String) -> Unit
) {

    var textoDeBusqueda by remember { mutableStateOf("") }
    var alimentoSeleccionado by remember { mutableStateOf<Alimento?>(null) }
    var cantidad by remember { mutableStateOf("") }
    var nutrientesCalculados by remember { mutableStateOf(NutrientesCalculados()) }
    val tiposDeComida = listOf("Desayuno", "Almuerzo", "Cena", "Snack")
    var tipoComida by remember { mutableStateOf(tiposDeComida[0]) }
    var tipoComidaExpanded by remember { mutableStateOf(false) }

    val listaFiltrada = remember(textoDeBusqueda) {
        if (textoDeBusqueda.length >= 2) {
            baseDeDatosAlimentos.filter { it.nombre.contains(textoDeBusqueda, ignoreCase = true) }
        } else {
            emptyList()
        }
    }
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
            .background(Color.Black.copy(alpha = 0.5f))
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
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = textoDeBusqueda,
                onValueChange = { textoDeBusqueda = it; alimentoSeleccionado = null },
                label = { Text("Buscar Alimento") },
                placeholder = { Text("Ej: Pechuga de pollo...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

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
            else if (listaFiltrada.isNotEmpty()) {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(listaFiltrada) { alimento ->
                        ResultadoBusquedaItem(
                            alimento = alimento,
                            estaSeleccionado = false,
                            onClick = {
                                alimentoSeleccionado = alimento
                                textoDeBusqueda = alimento.nombre
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it.filter { char -> char.isDigit() } },
                label = { Text("Cantidad (gramos)") },
                placeholder = { Text("Ej: 100") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = alimentoSeleccionado != null
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
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryEditable,
                            enabled = true
                        )
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
            val sePuedeGuardar = alimentoSeleccionado != null && (cantidad.toIntOrNull() ?: 0) > 0
            FormularioPrincipalButton(
                texto = "Guardar Comida",
                enabled = sePuedeGuardar,
                onClick = {
                    onGuardarComida(alimentoSeleccionado!!, cantidad.toInt(), tipoComida)
                }
            )
        }
    }
}


val baseDeDatosAlimentos = listOf(
    Alimento("Pechuga de pollo", 165, 31f, 0f, 3.6f),
    Alimento("Salmón", 208, 20f, 0f, 13f),
    Alimento("Huevo", 155, 13f, 1.1f, 11f),
    Alimento("Arroz blanco", 130, 2.7f, 28f, 0.3f),
    Alimento("Lentejas", 116, 9f, 20f, 0.4f),
    Alimento("Fresa", 32, 0.7f, 7.7f, 0.3f),
    Alimento("Sandía", 30, 0.6f, 7.6f, 0.2f),
    Alimento("Pan blanco", 265, 9f, 49f, 3.2f),
    Alimento("Aceite de Oliva", 884, 0f, 0f, 100f),
    Alimento("Salchicha", 301, 12f, 3f, 27f),
    Alimento("Queso Cheddar", 404, 25f, 1.3f, 33f)
)


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
            .background(if (estaSeleccionado) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(alimento.nombre, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = "Por 100g: ${alimento.caloriasPor100g} cal • ${alimento.proteinasPor100g}g P • ${alimento.carbosPor100g}g C • ${alimento.grasasPor100g}g G",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        if (estaSeleccionado) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Seleccionado",
                tint = Color.Green,
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
        Text("Valores: ", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${nutrientes.calorias} cal")
                }
                append(" • ${String.format("%.1f", nutrientes.proteinas)}g P")
                append(" • ${String.format("%.1f", nutrientes.carbos)}g C")
                append(" • ${String.format("%.1f", nutrientes.grasas)}g G")
            },
            color = Color.White,
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
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                MyText(
                    text = actividad.tipo,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = Color(0xFF3F2E65),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    MyText(
                        text = "${actividad.duracion} min",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.large),
                        contentDescription = "Calorías",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    MyText(
                        text = actividad.calorias.toString(),
                        color = Color.White,
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
                tint = Color.Red
            )
        }
    }
}


