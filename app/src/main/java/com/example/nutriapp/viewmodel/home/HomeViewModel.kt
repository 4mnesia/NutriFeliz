package com.example.nutriapp.viewmodel.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nutriapp.model.home.*
import java.util.UUID


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
    val maxCalorias: Int = 2200
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
    fun onGuardarComida(alimento: Alimento, cantidad: Int, tipoComida: String) { // Ahora recibe tipoComida
        val nuevaComida = ComidaAlacenada(
            alimento = alimento,
            cantidadEnGramos = cantidad,
            tipoDeComida = tipoComida
        )
        val nuevaListaComidas = uiState.listaComidas + nuevaComida
        recalcularEstadoDerivado(newState = uiState.copy(
            listaComidas = nuevaListaComidas, // Actualiza la lista
            formularioComidaAbierto = false
        ))
    }
    fun onBorrarComida(comida: ComidaAlacenada) {
        val nuevaListaComidas = uiState.listaComidas.filter { it.id != comida.id }
        recalcularEstadoDerivado(newState = uiState.copy(listaComidas = nuevaListaComidas))
    }
    private fun recalcularEstadoDerivado(newState: HomeUiState) {
        val caloriasQuemadas = newState.listaActividades.sumOf { it.calorias }

        val proteinasConsumidas = newState.listaComidas.sumOf {
            (it.alimento.proteinasPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val carbosConsumidos = newState.listaComidas.sumOf {
            (it.alimento.carbosPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val grasasConsumidas = newState.listaComidas.sumOf {
            (it.alimento.grasasPor100g * (it.cantidadEnGramos / 100.0)).toInt()
        }
        val caloriasConsumidas = (proteinasConsumidas * 4) + (carbosConsumidos * 4) + (grasasConsumidas * 9)
        val caloriasNetas = caloriasConsumidas - caloriasQuemadas
        val progresoCalorias = (caloriasNetas.toFloat() / newState.metaCalorias.toFloat()).coerceIn(0f, 1f)
        val progresoProteinas = (proteinasConsumidas.toFloat() / newState.metaProteinas.toFloat()).coerceIn(0f, 1f)
        val progresoCarbos = (carbosConsumidos.toFloat() / newState.metaCarbos.toFloat()).coerceIn(0f, 1f)
        val progresoGrasas = (grasasConsumidas.toFloat() / newState.metaGrasas.toFloat()).coerceIn(0f, 1f)
        uiState = newState.copy(
            proteinasConsumidas = proteinasConsumidas,
            carbosConsumidos = carbosConsumidos,
            grasasConsumidas = grasasConsumidas,
            caloriasQuemadas = caloriasQuemadas,
            caloriasConsumidas = caloriasConsumidas,
            caloriasNetas = caloriasNetas,
            progresoCalorias = progresoCalorias,
            progresoProteinas = progresoProteinas,
            progresoCarbos = progresoCarbos,
            progresoGrasas = progresoGrasas

        )
    }
}
