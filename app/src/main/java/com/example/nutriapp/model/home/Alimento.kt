package com.example.nutriapp.model.home

/**
 * Representa un alimento dentro de la lógica de la UI.
 * Esta clase es el "modelo" que usan los Composables y el ViewModel.
 */
data class Alimento(
    // Hacemos el id opcional (Long?) para poder manejar tanto alimentos que ya existen
    // en nuestro backend (y tienen un id) como alimentos nuevos de la API externa (sin id).
    val id: Long? = null,
    val nombre: String,
    val caloriasPor100g: Int,
    val proteinasPor100g: Float,
    val carbosPor100g: Float,
    val grasasPor100g: Float
)
