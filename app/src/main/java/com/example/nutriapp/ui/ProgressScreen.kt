// ... (importaciones existentes)
// Nuevas importaciones para Vico
package com.example.nutriapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.ui.theme.NutriAppTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
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
    //1. Prepara los datos del gráfico con ChartEntryModelProducer
    val chartEntryModelProducer = ChartEntryModelProducer(
        // Datos de ejemplo para cada línea
        listOf(
            // ... (tus datos no cambian)
            listOf(entryOf(0, 1800), entryOf(1, 2200), entryOf(2, 2100), entryOf(3, 2300), entryOf(4, 2250), entryOf(5, 2400), entryOf(6, 1800)),
            listOf(entryOf(0, 30), entryOf(1, 45), entryOf(2, 20), entryOf(3, 60), entryOf(4, 40), entryOf(5, 50), entryOf(6, 30)),
            listOf(entryOf(0, 70), entryOf(1, 70.2f), entryOf(2, 70.1f), entryOf(3, 69.8f), entryOf(4, 69.7f), entryOf(5, 69.5f), entryOf(6, 69.4f)),
            listOf(entryOf(0, 120), entryOf(1, 130), entryOf(2, 125), entryOf(3, 140), entryOf(4, 135), entryOf(5, 145), entryOf(6, 120))
        )
    )

    // 2. Formateador para el eje X (días de la semana)
    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        days.getOrElse(value.toInt()) { "" }
    }

    // 3. Dibuja el gráfico
    Chart(
        chart = lineChart(
            lines = listOf(
                // FIX: Convert the Compose Color to an integer ARGB value
                LineChart.LineSpec(lineColor = Color(0xFFFFA500).toArgb()), // Naranja para Calorías
                LineChart.LineSpec(lineColor = Color(0xFF8A2BE2).toArgb()), // Morado para Ejercicio
                LineChart.LineSpec(lineColor = Color(0xFF1E90FF).toArgb()), // Azul para Peso
                LineChart.LineSpec(lineColor = Color.Red.toArgb())          // Rojo para Proteínas
            )
        ),
        chartModelProducer = chartEntryModelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = bottomAxisValueFormatter,
            guideline = null
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFF24263A), shape = RoundedCornerShape(12.dp))
    )
}

@Composable
fun ChartLegend() {
    // Usamos FlowRow para que la leyenda se ajuste automáticamente si no cabe en una línea
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), // Espacio entre el gráfico y la leyenda
        horizontalArrangement = Arrangement.SpaceEvenly // Distribuye el espacio equitativamente
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
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        // El pequeño círculo de color
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = androidx.compose.foundation.shape.CircleShape)
        )
        // El texto de la leyenda
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
    // Make sure the theme name here is also correct.
    NutriAppTheme { // <--- Corrected name
        ProgressScreen()
    }
}
