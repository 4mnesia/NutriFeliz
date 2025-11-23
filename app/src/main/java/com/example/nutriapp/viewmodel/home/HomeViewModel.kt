package com.example.nutriapp.viewmodel.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.model.home.*
import com.example.nutriapp.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID


data class HomeUiState(
    // Actividades
    val listaActividades: List<Actividad> = emptyList(),
    val formularioActividadAbierto: Boolean = false,

    // Comidas
    val listaComidas: List<ComidaAlacenada> = emptyList(),
    val formularioComidaAbierto: Boolean = false,
    val proteinasConsumidas: Int = 0,
    val carbosConsumidos: Int = 0,
    val grasasConsumidas: Int = 0,

    // Pérdidas
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

    // Máximos
    val maxProteinas: Int = 250,
    val maxCarbos: Int = 500,
    val maxGrasas: Int = 150,
    val maxCalorias: Int = 2200,

    val weeklyCalories: Map<DayOfWeek, Int> = DayOfWeek.values().associateWith { 0 },
    val weightHistory: List<Float> = emptyList()
)

class HomeViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _sugerencias = MutableStateFlow<List<Alimento>>(emptyList())
    val sugerencias: StateFlow<List<Alimento>> = _sugerencias.asStateFlow()
    fun onToggleFormularioActividad() {
        _uiState.update { it.copy(formularioActividadAbierto = !it.formularioActividadAbierto) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun onBuscarSugerencias(query: String) {
        viewModelScope.launch {
            if (query.length >= 2) {
                val resultados = foodRepository.buscarSugerencias(query)
                _sugerencias.value = resultados
            } else {
                _sugerencias.value = emptyList()
            }
        }
    }

    fun onLimpiarSugerencias() {
        _sugerencias.value = emptyList()
    }
    
    // NUEVO: Función para buscar alimento en la API y guardar
    fun onBuscarYGuardarComida(query: String, cantidad: Int, tipoComida: String) {
        viewModelScope.launch {
            // Delegamos la búsqueda al repositorio
            val alimento = foodRepository.buscarAlimento(query)
            
            if (alimento != null) {
                // Si se encontró, lo guardamos
                onGuardarComida(alimento, cantidad, tipoComida)
            } else {
                // Manejo de error: no se encontró el alimento o hubo error de red
                println("No se encontraron alimentos para: $query")
            }
        }
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

    // ==================================================================
    // Guardar peso como lista secuencial
    // ==================================================================
    fun onSaveWeight(weight: String) {
        weight.toFloatOrNull()?.let { weightValue ->
            _uiState.update { currentState ->
                val updatedWeightHistory = currentState.weightHistory + weightValue
                currentState.copy(weightHistory = updatedWeightHistory)
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

class HomeViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}