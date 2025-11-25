package com.example.nutriapp.model.home

// Ya no necesitamos importar UUID
// import java.util.UUID

/**
 * Representa un alimento que ha sido "alacenado" o guardado en una comida específica (ej: 150g de Pollo en el Almuerzo).
 * Esta clase es el "modelo" que se muestra en la lista de comidas de la UI.
 */
data class ComidaAlacenada (
    // El ID ahora es un Long, porque viene del ID de la relación `ComidaAlimento` en la base de datos.
    val id: Long,
    val alimento: Alimento,
    val cantidadEnGramos: Int,
    val tipoDeComida: String
)
