package com.example.nutriapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.ui.theme.NutriAppTheme

// --- INICIO DE IMPORTS CORREGIDOS ---
// (Estos son para Vico 2.0.0-alpha.22)
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.dashed.dashedShape // Corregido
import com.patrykandpatrick.vico.compose.component.text.rememberTextComponent // Corregido
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineShape // Corregido
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
// --- FIN DE IMPORTS CORREGIDOS ---

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
            listOf(entryOf(0, 1500), entryOf(1, 1600), entryOf(2, 1550), entryOf(3, 1700), entryOf(4, 1650), entryOf(5, 1800), entryOf(6, 1750)),
            // Ejercicio (morado)
            listOf(entryOf(0, 20), entryOf(1, 30), entryOf(2, 25), entryOf(3, 120), entryOf(4, 30), entryOf(5, 45), entryOf(6, 15)),
            // Peso (azul)
            listOf(entryOf(0, 70), entryOf(1, 70.2f), entryOf(2, 70.1f), entryOf(3, 69.8f), entryOf(4, 69.7f), entryOf(5, 69.5f), entryOf(6, 69.4f)),
            // Proteínas (rojo)
            listOf(entryOf(0, 120), entryOf(1, 130), entryOf(2, 125), entryOf(3, 140), entryOf(4, 135), entryOf(5, 145), entryOf(6, 120))
        )
    )

    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        days.getOrElse(value.toInt()) { "" }
    }

    Chart(
        chart = lineChart(
            lines = listOf(
                LineChart.LineSpec(lineColor = Color(0xFFFFA500).toArgb()), // Naranja
                LineChart.LineSpec(lineColor = Color(0xFF8A2BE2).toArgb()), // Morado
                LineChart.LineSpec(lineColor = Color(0xFF1E90FF).toArgb()), // Azul
                LineChart.LineSpec(lineColor = Color.Red.toArgb())          // Rojo
            )
        ),
        chartModelProducer = chartEntryModelProducer,
        startAxis = rememberStartAxis(
            label = rememberTextComponent(color = Color.White),
            axis = null,
            tick = null,
            // --- CORRECCIÓN APLICADA ---
            guideline = dashedShape(
                shape = LineShape, // Corregido (sin "Shapes.")
                dashLength = 4.dp,
                gapLength = 4.dp,
                color = Color(0x40FFFFFF) // Blanco translúcido
            )
            // --- FIN DE LA CORRECCIÓN ---
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = bottomAxisValueFormatter,
            guideline = null,
            label = rememberTextComponent(color = Color.White),
            axis = null,
            tick = null
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