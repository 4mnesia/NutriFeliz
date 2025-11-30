package com.example.nutriapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // --- Endpoints de Autenticación ---
    @POST("api/usuarios/registro")
    suspend fun registerUser(@Body request: RegistrationRequest): Response<Void>

    @POST("api/usuarios/login")
    suspend fun loginUser(@Body request: LoginRequest): UsuarioDTO

    // --- Endpoints de Comidas y Alimentos ---
    @GET("api/comidas/usuario/{usuarioId}/fecha/{fecha}")
    suspend fun getComidasByUsuarioAndFecha(
        @Path("usuarioId") usuarioId: Long?,
        @Path("fecha") fecha: String
    ): HateoasComidaResponse

    @GET("api/comida-alimentos/comida/{comidaId}")
    suspend fun getAlimentosByComida(
        @Path("comidaId") comidaId: Long
    ): HateoasComidaAlimentoResponse

    @GET("api/alimentos/{id}")
    suspend fun getAlimentoById(
        @Path("id") alimentoId: Long
    ): AlimentoDTO

    @GET("api/alimentos/buscar")
    suspend fun buscarAlimentosEnBackend(
        @Query("nombre") query: String
    ): HateoasAlimentoResponse

    @POST("api/alimentos")
    suspend fun crearAlimento(@Body request: AlimentoCreateRequest): AlimentoDTO

    @POST("api/comidas")
    suspend fun crearComida(@Body request: ComidaCreateRequest): ComidaDTO

    @POST("api/comida-alimentos")
    suspend fun agregarAlimentoAComida(
        @Body request: ComidaAlimentoRequest
    ): ComidaAlimentoDTO

    // --- Endpoint NUEVO para Resumen Semanal Eficiente ---
    @GET("api/registros-diarios/usuario/{usuarioId}/semana")
    suspend fun getRegistrosSemanales(
        @Path("usuarioId") usuarioId: Long?
    ): HateoasRegistroDiarioResponse

    // --- Endpoints para Historial de Peso ---
    @GET("api/usuarios/{usuarioId}/historial-peso")
    suspend fun getHistorialPeso(
        @Path("usuarioId") usuarioId: Long?
    ): HateoasHistorialPesoResponse

    @POST("api/usuarios/{usuarioId}/historial-peso")
    suspend fun addPesoEntry(
        @Path("usuarioId") usuarioId: Long?,
        @Body request: NuevoPesoRequest
    ): HistorialPesoDTO

    // --- Endpoints para Usuario ---
    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(
        @Path("id") userId: Long?
    ): UsuarioDTO

    @PUT("api/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") userId: Long?,
        @Body request: UsuarioUpdateRequest
    ): UsuarioDTO
}
