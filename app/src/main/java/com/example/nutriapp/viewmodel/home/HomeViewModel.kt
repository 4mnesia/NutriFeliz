package com.example.nutriapp.viewmodel.home

import androidx.lifecycle.ViewModel
import com.example.nutriapp.model.home.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        val nuevaLista = _uiState.value.listaActividades + nuevaActividad
        recalcularEstadoDerivado(newState = _uiState.value.copy(
            listaActividades = nuevaLista,
            formularioActividadAbierto = false
        ))
    }
    fun onBorrarActividad(actividad: Actividad) {
        val nuevaLista = _uiState.value.listaActividades.filter { it.id != actividad.id }
        recalcularEstadoDerivado(newState = _uiState.value.copy(listaActividades = nuevaLista))
    }
    fun onToggleFormularioComida() {
        _uiState.update { it.copy(formularioComidaAbierto = !it.formularioComidaAbierto) }
    }
    fun onGuardarComida(alimento: Alimento, cantidad: Int, tipoComida: String) { // Ahora recibe tipoComida
        val nuevaComida = ComidaAlacenada(
            alimento = alimento,
            cantidadEnGramos = cantidad,
            tipoDeComida = tipoComida
        )
        val nuevaListaComidas = _uiState.value.listaComidas + nuevaComida
        recalcularEstadoDerivado(newState = _uiState.value.copy(
            listaComidas = nuevaListaComidas, // Actualiza la lista
            formularioComidaAbierto = false
        ))
    }
    fun onBorrarComida(comida: ComidaAlacenada) {
        val nuevaListaComidas = _uiState.value.listaComidas.filter { it.id != comida.id }
        recalcularEstadoDerivado(newState = _uiState.value.copy(listaComidas = nuevaListaComidas))
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
        _uiState.value = newState.copy(
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
