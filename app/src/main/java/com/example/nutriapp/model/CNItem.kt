package com.example.nutriapp.model

import com.example.nutriapp.model.home.Alimento

data class CNItem (
    val name: String,
    val calories: Float,
    val protein_g: Float,
    val carbohydrates_total_g: Float,
    val fat_total_g: Float
)
data class CNResponse(
    val items: List<CNItem>
)
fun CNItem.toAlimento(): Alimento {
    return Alimento(
        nombre = name,
        caloriasPor100g = calories.toInt(),
        proteinasPor100g = protein_g,
        carbosPor100g = carbohydrates_total_g,
        grasasPor100g = fat_total_g
    )
}
