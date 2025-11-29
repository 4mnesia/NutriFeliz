package com.example.nutriapp.viewmodel.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _sugerencias = MutableStateFlow<List<Alimento>>(emptyList())
    val sugerencias: StateFlow<List<Alimento>> = _sugerencias.asStateFlow()

    private val currentUserId = 1L
    private var searchJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
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
        }
    }

    fun onFoodQueryChange(query: String) {
        _uiState.update { it.copy(foodQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            if (query.length >= 2) {
                _sugerencias.value = foodRepository.buscarSugerencias(query)
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
        onLimpiarSugerencias()
    }

    // Modificado para usar la misma lógica de guardado y actualización optimista
    fun onSaveCurrentFood() {
        if (!_uiState.value.isFoodFormValid) return

        viewModelScope.launch {
            val state = _uiState.value
            val alimento = foodRepository.buscarAlimento(state.foodQuery)
            val cantidad = state.foodQuantity.toIntOrNull() ?: return@launch

            if (alimento != null) {
                onGuardarComida(alimento, cantidad, state.foodMealType)
                // Limpiar campos después de guardar
                 _uiState.update { it.copy(foodQuery = "", foodQuantity = "") }
            } else {
                // TODO: Manejar error de alimento no encontrado
            }
        }
    }
    
    fun onGuardarComida(alimento: Alimento, cantidad: Int, tipoComida: String) {
        val nuevaComida = ComidaAlacenada(
            id = UUID.randomUUID().mostSignificantBits, // ID temporal
            alimento = alimento,
            cantidadEnGramos = cantidad,
            tipoDeComida = tipoComida
        )

        viewModelScope.launch {
            // Actualización optimista de la UI
            _uiState.update {
                recalcularEstadoDerivado(
                    it.copy(
                        listaComidas = it.listaComidas + nuevaComida,
                        formularioComidaAbierto = false
                    )
                )
            }
            
            // Intentar guardar en backend
            val success = foodRepository.saveFoodInMeal(
                userId = currentUserId,
                alimento = alimento,
                cantidad = cantidad,
                tipoComida = tipoComida
            )
            
            if (success) {
                // Si tiene éxito, recargamos datos reales (opcional, para tener IDs correctos)
                // loadInitialData() 
            } else {
                // Si falla, podríamos revertir el cambio o mostrar un error
                // Por ahora lo dejamos así para que la UI parezca rápida
            }
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
