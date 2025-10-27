package com.example.nutriapp.model.home

import java.util.UUID

data class Actividad (
    val id: UUID = UUID.randomUUID(),
    val tipo: String,
    val duracion: Int,
    val calorias: Int
)