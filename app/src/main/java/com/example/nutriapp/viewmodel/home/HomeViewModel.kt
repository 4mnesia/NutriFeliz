package com.example.nutriapp.viewmodel.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.nutriapp.model.home.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

// REFACTORIZADO: Se ha corregido el estado para que no se pierdan los datos del peso.
data class HomeUiState(
    // Estado del tema
    val esTemaOscuro: Boolean = true,

    // Datos base de Actividad
    val listaActividades: List<Actividad> = emptyList(),
    val formularioActividadAbierto: Boolean = false,

    // Datos base de Comida
    val listaComidas: List<ComidaAlacenada> = emptyList(),
    val formularioComidaAbierto: Boolean = false,
    val proteinasConsumidas: Int = 0,
    val carbosConsumidos: Int = 0,
    val grasasConsumidas: Int = 0,

    //perdidas
    val caloriasQuemadas: Int = 0,
    val caloriasConsumidas: Int = 0,
    val caloriasNetas: Int = 0,

    // Progresos
    val progresoCalorias: Float = 0f,
    val progresoProteinas: Float = 0f,
    val progresoCarbos: Float = 0f,
    val progresoGrasas: Float = 0f,

    // Metas
    val metaProteinas: Int = 180,
    val metaCarbos: Int = 300,
    val metaGrasas: Int = 80,
    val metaCalorias: Int = 2000,

    // Max
    val maxProteinas: Int = 250,
    val maxCarbos: Int = 500,
    val maxGrasas: Int = 150,
    val maxCalorias: Int = 2200,

    // Datos para el gráfico semanal
    val weeklyCalories: Map<DayOfWeek, Int> = DayOfWeek.values().associateWith { 0 },

    // Datos para el gráfico mensual de peso
    val monthlyWeight: Map<LocalDate, Float> = emptyMap()
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onThemeChange() {
        _uiState.update { it.copy(esTemaOscuro = !it.esTemaOscuro) }
    }

    fun onToggleFormularioActividad() {
        _uiState.update { it.copy(formularioActividadAbierto = !it.formularioActividadAbierto) }
    }

    fun onGuardarActividad(tipo: String, duracion: Int) {
        val caloriasCalculadas = duracion * 12
        val nuevaActividad = Actividad(
            id = UUID.randomUUID(),
            tipo = tipo,
            duracion = duracion,
            calorias = caloriasCalculadas
        )
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(
                    listaActividades = it.listaActividades + nuevaActividad,
                    formularioActividadAbierto = false
                )
            )
        }
    }

    fun onBorrarActividad(actividad: Actividad) {
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(listaActividades = it.listaActividades.filter { a -> a.id != actividad.id })
            )
        }
    }

    fun onToggleFormularioComida() {
        _uiState.update { it.copy(formularioComidaAbierto = !it.formularioComidaAbierto) }
    }

    fun onGuardarComida(alimento: Alimento, cantidad: Int, tipoComida: String) {
        val nuevaComida = ComidaAlacenada(
            alimento = alimento,
            cantidadEnGramos = cantidad,
            tipoDeComida = tipoComida
        )
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(
                    listaComidas = it.listaComidas + nuevaComida,
                    formularioComidaAbierto = false
                )
            )
        }
    }

    fun onBorrarComida(comida: ComidaAlacenada) {
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(listaComidas = it.listaComidas.filter { c -> c.id != comida.id })
            )
        }
    }

    fun onSaveWeight(weight: String) {
        weight.toFloatOrNull()?.let { weightValue ->
            val today = LocalDate.now()
            _uiState.update { currentState ->
                val updatedWeight = currentState.monthlyWeight.toMutableMap()
                updatedWeight[today] = weightValue
                currentState.copy(monthlyWeight = updatedWeight)
            }
        }
    }

    fun onUpdateMacroGoals(calories: Int, protein: Int, carbs: Int, fat: Int) {
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(
                    metaCalorias = calories,
                    metaProteinas = protein,
                    metaCarbos = carbs,
                    metaGrasas = fat
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recalcularEstadoDerivado(currentState: HomeUiState): HomeUiState {
        val caloriasQuemadas = currentState.listaActividades.sumOf { it.calorias }

        val proteinasConsumidas = currentState.listaComidas.sumOf {
            (it.alimento.proteinasPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val carbosConsumidos = currentState.listaComidas.sumOf {
            (it.alimento.carbosPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val grasasConsumidas = currentState.listaComidas.sumOf {
            (it.alimento.grasasPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val caloriasConsumidas = (proteinasConsumidas * 4) + (carbosConsumidos * 4) + (grasasConsumidas * 9)
        val caloriasNetas = caloriasConsumidas - caloriasQuemadas

        val today = LocalDate.now().dayOfWeek
        val weeklyCalories = currentState.weeklyCalories.toMutableMap()
        weeklyCalories[today] = caloriasConsumidas

        val progresoCalorias = (caloriasNetas.toFloat() / currentState.metaCalorias.toFloat()).coerceIn(0f, 1f)
        val progresoProteinas = (proteinasConsumidas.toFloat() / currentState.metaProteinas.toFloat()).coerceIn(0f, 1f)
        val progresoCarbos = (carbosConsumidos.toFloat() / currentState.metaCarbos.toFloat()).coerceIn(0f, 1f)
        val progresoGrasas = (grasasConsumidas.toFloat() / currentState.metaGrasas.toFloat()).coerceIn(0f, 1f)

        return currentState.copy(
            proteinasConsumidas = proteinasConsumidas,
            carbosConsumidos = carbosConsumidos,
            grasasConsumidas = grasasConsumidas,
            caloriasQuemadas = caloriasQuemadas,
            caloriasConsumidas = caloriasConsumidas,
            caloriasNetas = caloriasNetas,
            progresoCalorias = progresoCalorias,
            progresoProteinas = progresoProteinas,
            progresoCarbos = progresoCarbos,
            progresoGrasas = progresoGrasas,
            weeklyCalories = weeklyCalories
        )
    }
}
