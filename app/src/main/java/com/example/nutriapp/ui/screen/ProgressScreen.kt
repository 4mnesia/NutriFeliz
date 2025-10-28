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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressScreen(username: String) {
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

        // Placeholder for a bar chart
        BarChartPlaceholder()

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder for a line chart
        LineChartPlaceholder()

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder for a circular chart
        CircularChartPlaceholder()
    }
}

@Composable
fun BarChartPlaceholder() {
    Column {
        Text(text = "Consumo de Calorías Semanal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            Bar(0.6f, "Lun")
            Bar(0.8f, "Mar")
            Bar(0.5f, "Mié")
            Bar(0.7f, "Jue")
            Bar(0.9f, "Vie")
            Bar(0.4f, "Sáb")
            Bar(0.6f, "Dom")
        }
    }
}

@Composable
fun Bar(heightFraction: Float, label: String) {
    val barColor = MaterialTheme.colorScheme.primary
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun LineChartPlaceholder() {
    Column {
        Text(text = "Pérdida de Peso Mensual", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val lineColor = MaterialTheme.colorScheme.secondary

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)) {
            val points = listOf(
                Offset(0f, size.height * 0.2f),
                Offset(size.width * 0.25f, size.height * 0.4f),
                Offset(size.width * 0.5f, size.height * 0.3f),
                Offset(size.width * 0.75f, size.height * 0.6f),
                Offset(size.width, size.height * 0.5f)
            )

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
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = lineColor,
                    start = points[i],
                    end = points[i+1],
                    strokeWidth = 5f
                )
            }
        }
    }
}

@Composable
fun CircularChartPlaceholder() {
    val proteinColor = MaterialTheme.colorScheme.primary
    val fatColor = MaterialTheme.colorScheme.secondary
    val carbsColor = MaterialTheme.colorScheme.tertiary

    val nutrientData = listOf(
        NutrientData("Proteína", 120f, proteinColor),
        NutrientData("Grasa", 70f, fatColor),
        NutrientData("Carbs", 250f, carbsColor)
    )

    val totalCalories = (nutrientData[0].value * 4) + (nutrientData[1].value * 9) + (nutrientData[2].value * 4)
    val totalGrams = nutrientData.sumOf { it.value.toDouble() }.toFloat()

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
