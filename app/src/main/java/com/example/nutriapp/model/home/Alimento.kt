package com.example.nutriapp.model.home

data class Alimento(
    val nombre: String,
    val caloriasPor100g: Int,
    val proteinasPor100g: Float,
    val carbosPor100g: Float,
    val grasasPor100g: Float
)