package com.example.nutriapp.ui.screen

// Imports para el gráfico
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalDensity

// Imports básicos de layout
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

// Imports de Material
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Imports de Compose
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Imports del ViewModel y Java Time
import com.example.nutriapp.viewmodel.home.HomeViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProgressScreen(username: String, homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Progreso de $username",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CircularChart(
            proteinas = uiState.proteinasConsumidas.toFloat(),
            grasas = uiState.grasasConsumidas.toFloat(),
            carbos = uiState.carbosConsumidos.toFloat()
        )

        Spacer(modifier = Modifier.height(32.dp))

        WeeklyBarChart(weeklyCalories = uiState.weeklyCalories, goalCalories = uiState.metaCalorias)

        Spacer(modifier = Modifier.height(32.dp))

        WeightLineChart(monthlyWeight = uiState.monthlyWeight)
    }
}

@Composable
fun WeeklyBarChart(weeklyCalories: Map<DayOfWeek, Int>, goalCalories: Int) {
    val today = LocalDate.now().dayOfWeek

    Column {
        Text(text = "Consumo de Calorías Semanal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            DayOfWeek.values().forEach { day ->
                val calories = weeklyCalories[day] ?: 0
                val progress = (calories.toFloat() / goalCalories.toFloat()).coerceIn(0f, 1f)
                Bar(
                    calories = calories,
                    heightFraction = progress,
                    label = day.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                    isToday = day == today
                )
            }
        }
    }
}

@Composable
fun Bar(calories: Int, heightFraction: Float, label: String, isToday: Boolean) {
    val barColor = if (isToday) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
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
                color = barColor,
                topLeft = Offset(x = 0f, y = size.height * (1 - heightFraction)),
                size = androidx.compose.ui.geometry.Size(size.width, size.height * heightFraction)
            )
        }
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = if (isToday) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun WeightLineChart(monthlyWeight: Map<LocalDate, Float>) {
    Column {
        Text(text = "Evolución de Peso", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (monthlyWeight.size < 2) {
            Text(
                text = "Necesitas registrar al menos dos pesos para ver tu progreso.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
            )
            return
        }

        val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val lineColor = Color.White
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

        val sortedData = monthlyWeight.entries.sortedBy { it.key }
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
                val minDayEpoch = sortedData.first().key.toEpochDay()
                val maxDayEpoch = sortedData.last().key.toEpochDay()
                val dayRange = (maxDayEpoch - minDayEpoch).toFloat().coerceAtLeast(1f)

                val maxWeight = monthlyWeight.values.maxOrNull() ?: 0f
                val minWeight = monthlyWeight.values.minOrNull() ?: 0f
                val weightRange = (maxWeight - minWeight).coerceAtLeast(1f)

                val points = sortedData.map { (date, weight) ->
                    val x = if (dayRange > 0) ((date.toEpochDay() - minDayEpoch).toFloat() / dayRange) * size.width else 0f
                    val y = if (weightRange > 0) size.height - ((weight - minWeight) / weightRange) * size.height else size.height / 2
                    Offset(x, y)
                }

                val numGridLines = 5
                for (i in 0 until numGridLines) {
                    val yPos = size.height * (i / (numGridLines - 1).toFloat())

                    drawLine(
                        color = gridColor,
                        start = Offset(0f, yPos),
                        end = Offset(size.width, yPos),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    val weightLabelValue = maxWeight - (weightRange * (i / (numGridLines - 1).toFloat()))
                    val weightText = "%.1f kg".format(weightLabelValue)
                    val textLayoutResult = textMeasurer.measure(weightText, style = textStyleYAxis)

                    drawText(
                        textMeasurer = textMeasurer,
                        text = weightText,
                        style = textStyleYAxis,
                        topLeft = Offset(
                            x = -(yAxisPaddingPx - innerOffsetPx),
                            y = yPos - (textLayoutResult.size.height / 2)
                        )
                    )
                }

                if (points.size > 1) {
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = lineColor,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 8f
                        )
                    }
                }

                points.forEach { offset ->
                    drawCircle(
                        color = lineColor,
                        radius = 8f,
                        center = offset,
                        style = Fill
                    )
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.5f),
                        radius = 8f,
                        center = offset,
                        style = Stroke(width = 2f)
                    )
                }
            }

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
                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
                    if (sortedData.isNotEmpty()) {
                        val firstDate = sortedData.first().key
                        Text(
                            text = firstDate.format(dateFormatter),
                            style = textStyleDate,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                    }
                    if (sortedData.size > 1) {
                        val lastDate = sortedData.last().key
                        Text(
                            text = lastDate.format(dateFormatter),
                            style = textStyleDate,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircularChart(proteinas: Float, grasas: Float, carbos: Float) {
    val proteinColor = MaterialTheme.colorScheme.background
    val fatColor = MaterialTheme.colorScheme.onSecondary
    val carbsColor = MaterialTheme.colorScheme.secondary

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
            Column {
                nutrientData.forEach { nutrient ->
                    LegendItem(name = "${nutrient.name} (${nutrient.value}g)", color = nutrient.color)
                }
            }
        }
    }
}

data class NutrientData(val name: String, val value: Float, val color: Color)

@Composable
fun LegendItem(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
    }
}
