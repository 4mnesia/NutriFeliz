package com.example.nutriapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec // Esta importación es correcta
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun ProgressScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Text(
            text = "Progreso Semanal",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Seguimiento de nutrición y peso",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        WeeklyProgressChart()
        ChartLegend()
    }
}

@Composable
fun WeeklyProgressChart() {
    val chartEntryModelProducer = ChartEntryModelProducer(
        listOf(
            // Calorías (naranja)
            listOf(entryOf(0, 1500f), entryOf(1, 1600f), entryOf(2, 1550f), entryOf(3, 1700f), entryOf(4, 1650f), entryOf(5, 1800f), entryOf(6, 1750f)),
            // Ejercicio (morado)
            listOf(entryOf(0, 20f), entryOf(1, 30f), entryOf(2, 25f), entryOf(3, 120f), entryOf(4, 30f), entryOf(5, 45f), entryOf(6, 15f)),
            // Peso (azul)
            listOf(entryOf(0, 70f), entryOf(1, 70.2f), entryOf(2, 70.1f), entryOf(3, 69.8f), entryOf(4, 69.7f), entryOf(5, 69.5f), entryOf(6, 69.4f)),
            // Proteínas (rojo)
            listOf(entryOf(0, 120f), entryOf(1, 130f), entryOf(2, 125f), entryOf(3, 140f), entryOf(4, 135f), entryOf(5, 145f), entryOf(6, 120f))
        )
    )

    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        days.getOrElse(value.toInt()) { "" }
    }

    Chart(
        chart = lineChart(
            lines = listOf(
                // 👇 CAMBIOS AQUÍ: color -> lineColor, thickness -> lineThickness
                lineSpec(lineColor = Color(0xFFFFA500), lineThickness = 2.dp),
                lineSpec(lineColor = Color(0xFF8A2BE2), lineThickness = 2.dp),
                lineSpec(lineColor = Color(0xFF1E90FF), lineThickness = 2.dp),
                lineSpec(lineColor = Color.Red, lineThickness = 2.dp)
            )
        ),
        chartModelProducer = chartEntryModelProducer,
        startAxis = rememberStartAxis(
            label = textComponent(color = Color.White),
            guideline = lineComponent(color = Color(0x40FFFFFF), thickness = 1.dp)
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = bottomAxisValueFormatter,
            label = textComponent(color = Color.White)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFF24263A), shape = RoundedCornerShape(12.dp))
    )
}

@Composable
fun ChartLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LegendItem(color = Color(0xFFFFA500), text = "Calorías")
        LegendItem(color = Color(0xFF8A2BE2), text = "Ejercicio (min)")
        LegendItem(color = Color(0xFF1E90FF), text = "Peso (kg)")
        LegendItem(color = Color.Red, text = "Proteínas")
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = CircleShape)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    NutriAppTheme {
        ProgressScreen()
    }
}