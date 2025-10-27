package com.example.nutriapp.model.home
import java.util.UUID
data class ComidaAlacenada (
    val id: UUID = UUID.randomUUID(),
    val alimento: Alimento,
    val cantidadEnGramos: Int,
    val tipoDeComida: String
)