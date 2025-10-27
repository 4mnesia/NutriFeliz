package com.example.nutriapp.viewmodel.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nutriapp.model.home.Actividad
import com.example.nutriapp.model.home.Alimento
import java.util.UUID


data class HomeUiState(
    // Estado del tema
    val esTemaOscuro: Boolean = false,

    // Datos base de Actividad
    val listaActividades: List<Actividad> = emptyList(),
    val formularioActividadAbierto: Boolean = false,

    // Datos base de Comida
    val formularioComidaAbierto: Boolean = false,
    val proteinasConsumidas: Int = 0,
    val carbosConsumidos: Int = 0,
    val grasasConsumidas: Int = 0,


    val caloriasQuemadas: Int = 0,
    val caloriasConsumidas: Int = 0,
    val caloriasNetas: Int = 0,
    val progresoProteinas: Float = 0f,
    val progresoCarbos: Float = 0f,
    val progresoGrasas: Float = 0f,

    // Metas
    val metaProteinas: Int = 180,
    val metaCarbos: Int = 300,
    val metaGrasas: Int = 80,
    val metaCalorias: Int = 2000
)

// 2. EL VIEWMODEL
class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set


    fun onThemeChange() {
        uiState = uiState.copy(esTemaOscuro = !uiState.esTemaOscuro)
    }


    fun onToggleFormularioActividad() {
        uiState = uiState.copy(formularioActividadAbierto = !uiState.formularioActividadAbierto)
    }

    fun onGuardarActividad(tipo: String, duracion: Int) {
        val caloriasCalculadas = duracion * 12
        val nuevaActividad = Actividad(
            id = UUID.randomUUID(),
            tipo = tipo,
            duracion = duracion,
            calorias = caloriasCalculadas
        )
        val nuevaLista = uiState.listaActividades + nuevaActividad
        recalcularEstadoDerivado(newState = uiState.copy(
            listaActividades = nuevaLista,
            formularioActividadAbierto = false
        ))
    }

    fun onBorrarActividad(actividad: Actividad) {
        uiState = uiState.copy(
            listaActividades = uiState.listaActividades.filter { it.id != actividad.id }
        )
    }

    fun onToggleFormularioComida() {
        uiState = uiState.copy(formularioComidaAbierto = !uiState.formularioComidaAbierto)
    }

    fun onGuardarComida(alimento: Alimento, cantidad: Int) {
        val ratio = cantidad / 100.0f
        uiState = uiState.copy(
            proteinasConsumidas = uiState.proteinasConsumidas + (alimento.proteinasPor100g * ratio).toInt(),
            carbosConsumidos = uiState.carbosConsumidos + (alimento.carbosPor100g * ratio).toInt(),
            grasasConsumidas = uiState.grasasConsumidas + (alimento.grasasPor100g * ratio).toInt(),
            formularioComidaAbierto = false
        )
    }
    private fun recalcularEstadoDerivado(newState: HomeUiState) {
        val caloriasQuemadas = newState.listaActividades.sumOf { it.calorias }
        val caloriasConsumidas = (newState.proteinasConsumidas * 4) + (newState.carbosConsumidos * 4) + (newState.grasasConsumidas * 9)
        val caloriasNetas = caloriasConsumidas - caloriasQuemadas
        val progresoProteinas = (newState.proteinasConsumidas.toFloat() / newState.metaProteinas.toFloat()).coerceIn(0f, 1f)
        val progresoCarbos = (newState.carbosConsumidos.toFloat() / newState.metaCarbos.toFloat()).coerceIn(0f, 1f)
        val progresoGrasas = (newState.grasasConsumidas.toFloat() / newState.metaGrasas.toFloat()).coerceIn(0f, 1f)

        uiState = newState.copy(
            caloriasQuemadas = caloriasQuemadas,
            caloriasConsumidas = caloriasConsumidas,
            caloriasNetas = caloriasNetas,
            progresoProteinas = progresoProteinas,
            progresoCarbos = progresoCarbos,
            progresoGrasas = progresoGrasas
        )
    }
}
