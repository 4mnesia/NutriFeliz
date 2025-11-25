package com.example.nutriapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.viewmodel.home.HomeViewModel
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

/**
 * Composable que representa la pantalla de "Progreso".
 * Muestra visualizaciones y gráficos sobre la evolución y el estado actual del usuario.
 *
 * @param username El nombre del usuario a mostrar en el título.
 * @param homeViewModel ViewModel compartido que proporciona los datos de progreso (historial de peso, calorías, etc.).
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgressScreen(username: String, homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Progreso de $username",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Gráfico circular para la distribución de macronutrientes del día.
        CircularChart(
            proteinas = uiState.proteinasConsumidas.toFloat(),
            grasas = uiState.grasasConsumidas.toFloat(),
            carbos = uiState.carbosConsumidos.toFloat()
        )

        // Gráfico de barras para el consumo de calorías de la última semana.
        WeeklyBarChart(
            weeklyCalories = uiState.weeklyCalories,
            goalCalories = uiState.metaCalorias
        )

        // Gráfico de líneas para la evolución del peso del usuario.
        WeightLineChart(weightHistory = uiState.weightHistory)
    }
}

/**
 * Muestra un gráfico de barras con el consumo de calorías para cada día de la semana.
 * Este Composable es "consciente del tiempo" y actualizará la barra de "hoy" automáticamente si el día cambia.
 *
 * @param weeklyCalories Un mapa que asocia cada [DayOfWeek] con las calorías consumidas.
 * @param goalCalories La meta de calorías diarias del usuario para calcular el progreso de cada barra.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyBarChart(weeklyCalories: Map<DayOfWeek, Int>, goalCalories: Int) {
    // Estado para saber cuál es el día de hoy. Se actualizará si cambia.
    var today by remember { mutableStateOf(LocalDate.now().dayOfWeek) }

    // Efecto que se ejecuta en un bucle para comprobar si el día ha cambiado.
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000) // Comprueba cada 60 segundos
            val currentDay = LocalDate.now().dayOfWeek
            if (today != currentDay) {
                today = currentDay
            }
        }
    }

    Column {
        Text(text = "Consumo de Calorías Semanal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            // Itera sobre todos los días de la semana para dibujar una barra para cada uno.
            DayOfWeek.values().forEach { day ->
                val calories = weeklyCalories[day] ?: 0
                val progress = (calories.toFloat() / goalCalories.toFloat()).coerceIn(0f, 1f)
                Bar(
                    calories = calories,
                    heightFraction = progress,
                    label = day.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                    isToday = day == today // Usa el estado que se actualiza automáticamente
                )
            }
        }
    }
}

/**
 * Dibuja una única barra del gráfico, representando el consumo de un día.
 *
 * @param calories El número de calorías a mostrar sobre la barra.
 * @param heightFraction La altura de la barra como una fracción (0.0 a 1.0) de la altura máxima.
 * @param label La etiqueta a mostrar debajo de la barra (ej. "Lun").
 * @param isToday Si es `true`, la barra se pinta con un color de acento para destacarla.
 */
@Composable
fun Bar(calories: Int, heightFraction: Float, label: String, isToday: Boolean) {
    val barColor = if (isToday) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = calories.toString(),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(
            modifier = Modifier
                .height(100.dp)
                .width(30.dp)
        ) {
            drawRect(
                color = barColor.copy(alpha = 0.5f), // Color base
                topLeft = Offset(x = 0f, y = 0f),
                size = size
            )
            drawRect(
                color = barColor, // Color del progreso
                topLeft = Offset(x = 0f, y = size.height * (1 - heightFraction)),
                size = androidx.compose.ui.geometry.Size(size.width, size.height * heightFraction)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isToday) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Muestra un gráfico de líneas con la evolución del peso del usuario a lo largo del tiempo.
 *
 * @param weightHistory Una lista de valores [Float] que representan los registros de peso.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeightLineChart(weightHistory: List<Float>) {
    Column {
        Text(
            text = "Evolución de Peso",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (weightHistory.size < 2) {
            Text(
                text = if (weightHistory.isEmpty()) "Aún no hay registros de peso." else "Se necesita al menos dos registros para mostrar la evolución.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            )
            return
        }

        val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val lineColor = MaterialTheme.colorScheme.tertiary
        val textColor = MaterialTheme.colorScheme.onSurface
        val textMeasurer = rememberTextMeasurer()

        val textStyleYAxis = TextStyle(
            color = textColor,
            fontSize = 10.sp,
            textAlign = TextAlign.End
        )

        val textStyleDate = TextStyle(
            color = textColor,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )

        val displayData = if (weightHistory.size > 60) weightHistory.takeLast(60) else weightHistory

        val maxWeight = displayData.maxOrNull() ?: 1f
        val minWeight = displayData.minOrNull() ?: 0f
        val weightRange = (maxWeight - minWeight).coerceAtLeast(1f)

        val yAxisLabelPadding = 40.dp
        val density = LocalDensity.current
        val yAxisPaddingPx = with(density) { yAxisLabelPadding.toPx() }
        val innerOffsetPx = with(density) { 4.dp.toPx() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 20.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = yAxisLabelPadding)
            ) {
                val pointCount = displayData.size

                val points = displayData.mapIndexed { index, weight ->
                    val x = if (pointCount > 1) (index.toFloat() / (pointCount - 1)) * size.width else size.width / 2f
                    val y = size.height - ((weight - minWeight) / weightRange) * size.height
                    Offset(x, y)
                }

                // Dibuja las líneas de la cuadrícula del eje Y con etiquetas de peso.
                val numGridLines = 5
                for (i in 0 until numGridLines) {
                    val yPos = size.height * (i / (numGridLines - 1).toFloat())
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, yPos),
                        end = Offset(size.width, yPos),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )

                    val weightLabelValue = maxWeight - (weightRange * (i / (numGridLines - 1).toFloat()))
                    val weightText = "%.1f kg".format(weightLabelValue)
                    val textLayoutResult = textMeasurer.measure(weightText, style = textStyleYAxis)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = weightText,
                        style = textStyleYAxis,
                        topLeft = Offset(-(yAxisPaddingPx - innerOffsetPx), yPos - (textLayoutResult.size.height / 2))
                    )
                }

                // Dibuja la línea de evolución del peso.
                if (points.size >= 2) {
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = lineColor,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 5f
                        )
                    }
                }

                // Dibuja los puntos sobre la línea.
                points.forEach { offset ->
                    drawCircle(color = lineColor, radius = 6f, center = offset, style = Fill)
                    drawCircle(color = Color.Black.copy(alpha = 0.5f), radius = 6f, center = offset, style = Stroke(width = 1.5f))
                }
            }

            // Dibuja las etiquetas de "Inicio" y "Último" en el eje X.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = yAxisLabelPadding, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Inicio", style = textStyleDate, textAlign = TextAlign.Start)
                    Text(text = "Último", style = textStyleDate, textAlign = TextAlign.End)
                }
            }
        }
    }
}

/**
 * Muestra un gráfico de tarta con la distribución de los macronutrientes.
 */
@Composable
fun CircularChart(proteinas: Float, grasas: Float, carbos: Float) {
    val proteinColor = Color(0xFFF44336) // Rojo para proteínas
    val fatColor = Color(0xFFFFEB3B)     // Amarillo para grasas
    val carbsColor = Color(0xFF4CAF50)    // Verde para carbohidratos

    val nutrientData = listOf(
        NutrientData("Proteína", proteinas, proteinColor),
        NutrientData("Grasa", grasas, fatColor),
        NutrientData("Carbs", carbos, carbsColor)
    )

    val totalCalories = (proteinas * 4) + (grasas * 9) + (carbos * 4)
    val totalGrams = (proteinas + grasas + carbos).coerceAtLeast(1f)

    Column {
        Text(text = "Distribución de Macronutrientes", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(150.dp)) {
                    if (totalGrams > 0) {
                        var startAngle = -90f
                        nutrientData.forEach { nutrient ->
                            val sweepAngle = (nutrient.value / totalGrams) * 360f
                            drawArc(
                                color = nutrient.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = 25f)
                            )
                            startAngle += sweepAngle
                        }
                    } else { // Dibuja un círculo gris si no hay datos
                        drawArc(
                            color = Color.LightGray,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 25f)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.0f".format(totalCalories),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(text = "Kcal", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            // Leyenda del gráfico circular
            Column {
                nutrientData.forEach { nutrient ->
                    LegendItem(name = "${nutrient.name} (${String.format("%.1f", nutrient.value)}g)", color = nutrient.color)
                }
            }
        }
    }
}

private data class NutrientData(val name: String, val value: Float, val color: Color)

/**
 * Un único elemento de la leyenda para el gráfico circular.
 */
@Composable
fun LegendItem(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = MaterialTheme.shapes.small)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
    }
}
