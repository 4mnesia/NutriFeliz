package com.example.nutriapp.viewmodel.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.data.SessionManager
import com.example.nutriapp.model.home.*
import com.example.nutriapp.repository.FoodRepository
import com.example.nutriapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val listaActividades: List<Actividad> = emptyList(),
    val formularioActividadAbierto: Boolean = false,
    val listaComidas: List<ComidaAlacenada> = emptyList(),
    val formularioComidaAbierto: Boolean = false,
    val foodQuery: String = "",
    val foodQuantity: String = "",
    val foodMealType: String = "Desayuno",
    val proteinasConsumidas: Int = 0,
    val carbosConsumidos: Int = 0,
    val grasasConsumidas: Int = 0,
    val caloriasQuemadas: Int = 0,
    val caloriasConsumidas: Int = 0,
    val caloriasNetas: Int = 0,
    val progresoCalorias: Float = 0f,
    val progresoProteinas: Float = 0f,
    val progresoCarbos: Float = 0f,
    val progresoGrasas: Float = 0f,
    val metaProteinas: Int = 180,
    val metaCarbos: Int = 300,
    val metaGrasas: Int = 80,
    val metaCalorias: Int = 2000,
    val maxProteinas: Int = 250,
    val maxCarbos: Int = 500,
    val maxGrasas: Int = 150,
    val maxCalorias: Int = 2200,
    val weeklyCalories: Map<DayOfWeek, Int> = emptyMap(),
    val weightHistory: List<Float> = emptyList()
) {
    val isFoodFormValid: Boolean
        get() = foodQuery.isNotBlank() && foodQuantity.toIntOrNull()?.let { it > 0 } ?: false
}

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sugerencias = MutableStateFlow<List<Alimento>>(emptyList())
    val sugerencias: StateFlow<List<Alimento>> = _sugerencias.asStateFlow()

    private var currentUserId : Long? = null


    private var searchJob: Job? = null

    init {
        // 3. Observar el ID del usuario apenas se crea el ViewModel
        viewModelScope.launch {
            sessionManager.userId.collect { id ->
                if (id != null) {
                    currentUserId = id
                    loadInitialData()
                }
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val weeklyDataJob = async { foodRepository.getWeeklyCalories(currentUserId) }
                val weightDataJob = async { userRepository.getWeightHistory(currentUserId) }
                val userDataJob = async { userRepository.getUserData(currentUserId) }
                val todaysMealsJob = async { foodRepository.getTodaysMeals(currentUserId) }

                val weeklyData = weeklyDataJob.await()
                val weightData = weightDataJob.await()
                val userData = userDataJob.await()
                val todaysMeals = todaysMealsJob.await()

                _uiState.update {
                    val updatedState = it.copy(
                        weeklyCalories = weeklyData,
                        weightHistory = weightData,
                        listaComidas = todaysMeals,
                        metaCalorias = userData?.metaCalorias ?: it.metaCalorias,
                        metaProteinas = userData?.metaProteinas ?: it.metaProteinas,
                        metaCarbos = userData?.metaCarbos ?: it.metaCarbos,
                        metaGrasas = userData?.metaGrasas ?: it.metaGrasas
                    )
                    recalcularEstadoDerivado(updatedState)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading initial data", e)
            }
        }
    }

    fun onFoodQueryChange(query: String) {
        _uiState.update { it.copy(foodQuery = query) }
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            if (query.length >= 2) {
                val resultados = foodRepository.buscarSugerencias(query)
                _sugerencias.value = resultados
            } else {
                _sugerencias.value = emptyList()
            }
        }
    }

    fun onFoodQuantityChange(quantity: String) {
        _uiState.update { it.copy(foodQuantity = quantity) }
    }

    fun onFoodMealTypeChange(mealType: String) {
        _uiState.update { it.copy(foodMealType = mealType) }
    }

    fun onSuggestionClicked(alimento: Alimento) {
        _uiState.update { it.copy(foodQuery = alimento.nombre) }
        _sugerencias.value = emptyList()
    }

    fun onSaveCurrentFood() {
        if (!_uiState.value.isFoodFormValid) return

        viewModelScope.launch {
            val state = _uiState.value
            val alimento = foodRepository.buscarAlimento(state.foodQuery)
            val cantidad = state.foodQuantity.toIntOrNull() ?: return@launch

            if (alimento != null) {
                onGuardarComida(alimento, cantidad, state.foodMealType)
            }
        }
    }

    fun onGuardarComida(alimento: Alimento, cantidad: Int, tipoComida: String) {
        val nuevaComida = ComidaAlacenada(
            id = UUID.randomUUID().mostSignificantBits,
            alimento = alimento,
            cantidadEnGramos = cantidad,
            tipoDeComida = tipoComida
        )

        // Actualización optimista: Agregamos el item a la UI inmediatamente
        _uiState.update {
            recalcularEstadoDerivado(
                it.copy(
                    listaComidas = it.listaComidas + nuevaComida,
                    formularioComidaAbierto = false,
                    foodQuery = "",
                    foodQuantity = ""
                )
            )
        }
        _sugerencias.value = emptyList()

        viewModelScope.launch {
            val success = foodRepository.guardarAlimentoEnBackend(
                userId = currentUserId,
                alimento = alimento,
                cantidadIngresada = cantidad, // Asegúrate que el nombre del parámetro coincida con el Repo
                tipoComida = tipoComida
            )

            if (success) {
                Log.d("HomeViewModel", "Guardado exitoso en backend")
                // Opcional: Podrías recargar datos reales si quieres estar 100% sincronizado
                // loadInitialData()
            } else {
                // Rollback si falla: eliminamos el item que habíamos agregado visualmente
                Log.e("HomeViewModel", "Fallo al guardar en backend. Revertiendo UI.")
                _uiState.update { state ->
                    val listaCorregida = state.listaComidas.filter { it.id != nuevaComida.id }
                    recalcularEstadoDerivado(state.copy(listaComidas = listaCorregida))
                }
            }
        }
    }
    fun onBuscarEnApi(query: String) {
        viewModelScope.launch {
            val resultados = foodRepository.buscarEnApiExterna(query)
            _sugerencias.value = resultados
        }
    }

    fun onToggleFormularioComida() {
        _uiState.update {
            if (it.formularioComidaAbierto) {
                it.copy(formularioComidaAbierto = false, foodQuery = "", foodQuantity = "", foodMealType = "Desayuno")
            } else {
                it.copy(formularioComidaAbierto = true)
            }
        }
    }

    fun onSaveWeight(weight: String) {
        weight.toDoubleOrNull()?.let { weightValue ->
            viewModelScope.launch {
                val success = userRepository.saveNewWeight(currentUserId, weightValue)
                if (success) loadInitialData()
            }
        }
    }

    fun onUpdateMacroGoals(calories: Int, protein: Int, carbs: Int, fat: Int) {
        viewModelScope.launch {
            val success = userRepository.updateUserGoals(currentUserId, calories, protein, carbs, fat)
            if (success) {
                _uiState.update { it.copy(metaCalorias = calories, metaProteinas = protein, metaCarbos = carbs, metaGrasas = fat) }
            }
        }
    }

    fun onToggleFormularioActividad() {
        _uiState.update { it.copy(formularioActividadAbierto = !it.formularioActividadAbierto) }
    }

    fun onGuardarActividad(tipo: String, duracion: Int) {
        val nuevaActividad = Actividad(id = UUID.randomUUID(), tipo = tipo, duracion = duracion, calorias = duracion * 12)
        _uiState.update { recalcularEstadoDerivado(it.copy(listaActividades = it.listaActividades + nuevaActividad, formularioActividadAbierto = false)) }
    }

    fun onBorrarActividad(actividad: Actividad) {
        _uiState.update { recalcularEstadoDerivado(it.copy(listaActividades = it.listaActividades.filter { a -> a.id != actividad.id })) }
    }

    fun onLimpiarSugerencias() {
        _sugerencias.value = emptyList()
    }

    fun onBorrarComida(comida: ComidaAlacenada) {
        _uiState.update { recalcularEstadoDerivado(it.copy(listaComidas = it.listaComidas.filter { c -> c.id != comida.id })) }
    }

    private fun recalcularEstadoDerivado(currentState: HomeUiState): HomeUiState {
        val caloriasQuemadas = currentState.listaActividades.sumOf { it.calorias }
        val proteinasConsumidas = currentState.listaComidas.sumOf { (it.alimento.proteinasPor100g * (it.cantidadEnGramos / 100.0)).toInt() }
        val carbosConsumidos = currentState.listaComidas.sumOf { (it.alimento.carbosPor100g * (it.cantidadEnGramos / 100.0)).toInt() }
        val grasasConsumidas = currentState.listaComidas.sumOf { (it.alimento.grasasPor100g * (it.cantidadEnGramos / 100.0)).toInt() }
        val caloriasConsumidas = (proteinasConsumidas * 4) + (carbosConsumidos * 4) + (grasasConsumidas * 9)
        val caloriasNetas = caloriasConsumidas - caloriasQuemadas
        val progresoCalorias = if (currentState.metaCalorias > 0) (caloriasNetas.toFloat() / currentState.metaCalorias.toFloat()).coerceIn(0f, 1f) else 0f
        val progresoProteinas = if (currentState.metaProteinas > 0) (proteinasConsumidas.toFloat() / currentState.metaProteinas.toFloat()).coerceIn(0f, 1f) else 0f
        val progresoCarbos = if (currentState.metaCarbos > 0) (carbosConsumidos.toFloat() / currentState.metaCarbos.toFloat()).coerceIn(0f, 1f) else 0f
        val progresoGrasas = if (currentState.metaGrasas > 0) (grasasConsumidas.toFloat() / currentState.metaGrasas.toFloat()).coerceIn(0f, 1f) else 0f
        
        val today = LocalDate.now().dayOfWeek
        val weeklyCalories = currentState.weeklyCalories.toMutableMap()
        weeklyCalories[today] = caloriasConsumidas

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
