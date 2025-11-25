package com.example.nutriapp.network

import com.google.gson.annotations.SerializedName

// --- DTOs de la Aplicación (para recibir datos) ---

data class AlimentoDTO(
    val id: Long,
    val nombre: String,
    @SerializedName("caloriasPor100g") val calorias: Int,
    @SerializedName("proteinasPor100g") val proteinas: Float,
    @SerializedName("carbosPor100g") val carbos: Float,
    @SerializedName("grasasPor100g") val grasas: Float
)

data class ComidaDTO(
    val id: Long,
    val tipoDeComida: String,
    val fecha: String,
    val usuarioId: Long
)

data class ComidaAlimentoDTO(
    val id: Long,
    val comidaId: Long,
    val alimentoId: Long,
    val cantidadEnGramos: Int
)

data class HistorialPesoDTO(
    val id: Long,
    val peso: Double,
    val fecha: String
)

data class UsuarioDTO(
    val id: Long,
    val fullName: String,
    val username: String,
    val email: String,
    val peso: Double?,
    val metaCalorias: Int?,
    val metaProteinas: Int?,
    val metaCarbos: Int?,
    val metaGrasas: Int?
)

// --- DTO NUEVO para el Resumen Diario ---
data class RegistroDiarioDTO(
    val id: Long,
    val fecha: String,
    val totalCalorias: Int,
    val totalProteinas: Int,
    val totalCarbos: Int,
    val totalGrasas: Int
)


// --- DTOs para Requests (para enviar datos) ---

data class NuevoPesoRequest(
    val peso: Double
)

data class UsuarioUpdateRequest(
    val metaCalorias: Int,
    val metaProteinas: Int,
    val metaCarbos: Int,
    val metaGrasas: Int
)

data class LoginRequest(
    val usernameOrEmail: String,
    val passwordHash: String
)

data class RegistrationRequest(
    val fullName: String,
    val username: String,
    val email: String,
    val passwordHash: String
)

data class UsuarioIdWrapper(val id: Long)
data class ComidaCreateRequest(
    val tipoDeComida: String,
    val fecha: String,
    val usuario: UsuarioIdWrapper
)

data class ComidaIdWrapper(val id: Long)
data class AlimentoIdWrapper(val id: Long)
data class ComidaAlimentoRequest(
    val comida: ComidaIdWrapper,
    val alimento: AlimentoIdWrapper,
    val cantidadEnGramos: Int
)


// --- HATEOAS Wrappers (para recibir listas) ---

data class EmbeddedAlimentos(
    @SerializedName("alimentoDTOList") val alimentos: List<AlimentoDTO>?
)
data class HateoasAlimentoResponse(
    @SerializedName("_embedded") val embedded: EmbeddedAlimentos?
)

data class EmbeddedComidas(
    @SerializedName("comidaDTOList") val comidas: List<ComidaDTO>?
)
data class HateoasComidaResponse(
    @SerializedName("_embedded") val embedded: EmbeddedComidas?
)

data class EmbeddedComidaAlimentos(
    @SerializedName("comidaAlimentoDTOList") val comidaAlimentos: List<ComidaAlimentoDTO>?
)
data class HateoasComidaAlimentoResponse(
    @SerializedName("_embedded") val embedded: EmbeddedComidaAlimentos?
)

data class EmbeddedHistorialPeso(
    @SerializedName("historialPesoDTOList") val historial: List<HistorialPesoDTO>?
)
data class HateoasHistorialPesoResponse(
    @SerializedName("_embedded") val embedded: EmbeddedHistorialPeso?
)

data class EmbeddedUsuarios(
    @SerializedName("usuarioDTOList") val usuarios: List<UsuarioDTO>?
)
data class HateoasUsuarioResponse(
    @SerializedName("_embedded") val embedded: EmbeddedUsuarios?
)

// --- Wrapper NUEVO para Registro Diario ---
data class EmbeddedRegistrosDiarios(
    @SerializedName("registroDiarioDTOList") val registros: List<RegistroDiarioDTO>?
)
data class HateoasRegistroDiarioResponse(
    @SerializedName("_embedded") val embedded: EmbeddedRegistrosDiarios?
)
