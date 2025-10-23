package com.example.nutriapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data classes para representar el estado de la UI de forma clara
data class CalorieStatus(val current: Int = 0, val goal: Int = 2000, val max: Int = 2200)
data class MacroStatus(val current: Int = 0, val goal: Int)
data class ActivityStatus(val caloriesBurned: Int = 0)

data class HomeUiState(
    val calorieStatus: CalorieStatus = CalorieStatus(),
    val proteinStatus: MacroStatus = MacroStatus(goal = 180),
    val carbStatus: MacroStatus = MacroStatus(goal = 300),
    val fatStatus: MacroStatus = MacroStatus(goal = 80),
    val activityStatus: ActivityStatus = ActivityStatus(),
    val hasActivities: Boolean = false // Para mostrar el mensaje "No hay actividades"
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Aquí, en el futuro, irían las funciones para añadir comidas, actividades, etc.
    // fun addMeal(calories: Int, protein: Int, carbs: Int, fat: Int) { ... }
    // fun addActivity(caloriesBurned: Int) { ... }
}
