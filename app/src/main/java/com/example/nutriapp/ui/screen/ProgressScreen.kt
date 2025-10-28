package com.example.nutriapp.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.viewmodel.home.HomeViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ProgressScreen(username: String, homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Progreso de $username",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Gráfico circular conectado al ViewModel
        CircularChart(
            proteinas = uiState.proteinasConsumidas.toFloat(),
            grasas = uiState.grasasConsumidas.toFloat(),
            carbos = uiState.carbosConsumidos.toFloat()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Gráfico de barras semanal conectado al ViewModel
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
                    label = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
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
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
            )
            return
        }

        val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val lineColor = MaterialTheme.colorScheme.secondary

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)) {
            val sortedData = monthlyWeight.entries.sortedBy { it.key }
            val maxDay = sortedData.last().key.toEpochDay()
            val minDay = sortedData.first().key.toEpochDay()
            val dayRange = (maxDay - minDay).toFloat().coerceAtLeast(1f)

            val maxWeight = monthlyWeight.values.maxOrNull() ?: 0f
            val minWeight = monthlyWeight.values.minOrNull() ?: 0f
            val weightRange = (maxWeight - minWeight).coerceAtLeast(1f)

            val points = sortedData.map { (date, weight) ->
                val x = if (dayRange > 0) ((date.toEpochDay() - minDay).toFloat() / dayRange) * size.width else 0f
                val y = if (weightRange > 0) size.height - ((weight - minWeight) / weightRange) * size.height else size.height / 2
                Offset(x, y)
            }

            // Draw grid lines
            for (i in 0..4) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, size.height * (i / 4f)),
                    end = Offset(size.width, size.height * (i / 4f)),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            }

            // Draw the line chart
            if (points.size > 1) {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = lineColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 5f
                    )
                }
            }
        }
    }
}

@Composable
fun CircularChart(proteinas: Float, grasas: Float, carbos: Float) {
    val proteinColor = MaterialTheme.colorScheme.primary
    val fatColor = MaterialTheme.colorScheme.secondary
    val carbsColor = MaterialTheme.colorScheme.tertiary

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
        Box(modifier = Modifier
            .size(12.dp)
            .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
    }
}
