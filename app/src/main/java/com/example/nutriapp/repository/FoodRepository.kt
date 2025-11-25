package com.example.nutriapp.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nutriapp.model.home.Alimento
import com.example.nutriapp.model.home.ComidaAlacenada
import com.example.nutriapp.model.toAlimento
import com.example.nutriapp.network.*
import com.example.nutriapp.remote.ApiServiceFood
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val externalApiService: ApiServiceFood,
    private val backendApiService: ApiService
) {

    /**
     * Busca un alimento. Primero intenta en el backend y si no, en la API externa.
     */
    suspend fun buscarAlimento(query: String): Alimento? {
        return try {
            val response = backendApiService.buscarAlimentosEnBackend(query)
            response.embedded?.alimentos?.firstOrNull()?.let {
                Alimento(id = it.id, nombre = it.nombre, caloriasPor100g = it.calorias, proteinasPor100g = it.proteinas, carbosPor100g = it.carbos, grasasPor100g = it.grasas)
            } ?: buscarAlimentoExterno(query) // Fallback si el backend no devuelve nada
        } catch (e: Exception) {
            e.printStackTrace()
            buscarAlimentoExterno(query) // Fallback si el backend falla
        }
    }

    private suspend fun buscarAlimentoExterno(query: String): Alimento? {
        return try {
            externalApiService.getFoodDetails(query).items.firstOrNull()?.toAlimento()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- ¡FUNCIÓN CORREGIDA! ---
    /**
     * Busca sugerencias de alimentos. Prioriza el backend y si no hay resultados, usa la API externa.
     */
    suspend fun buscarSugerencias(query: String): List<Alimento> {
        return try {
            // 1. Intentar buscar en nuestro backend primero.
            val backendResponse = backendApiService.buscarAlimentosEnBackend(query)
            val backendAlimentos = backendResponse.embedded?.alimentos?.map {
                Alimento(id = it.id, nombre = it.nombre, caloriasPor100g = it.calorias, proteinasPor100g = it.proteinas, carbosPor100g = it.carbos, grasasPor100g = it.grasas)
            }

            // 2. Si el backend devuelve resultados, los usamos.
            if (!backendAlimentos.isNullOrEmpty()) {
                return backendAlimentos
            }

            // 3. Si el backend no tiene resultados, hacemos fallback a la API externa.
            externalApiService.getFoodDetails(query).items.map { it.toAlimento() }

        } catch (e: Exception) {
            e.printStackTrace()
            // Si todo falla, intentamos la API externa como último recurso.
            try {
                externalApiService.getFoodDetails(query).items.map { it.toAlimento() }
            } catch (e2: Exception) {
                e2.printStackTrace()
                emptyList()
            }
        }
    }

    // --- (El resto de las funciones no cambian) ---

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getWeeklyCalories(userId: Long): Map<DayOfWeek, Int> {
        val weeklyCalories = mutableMapOf<DayOfWeek, Int>()
        DayOfWeek.values().forEach { day -> weeklyCalories[day] = 0 }

        return try {
            val response = backendApiService.getRegistrosSemanales(userId)
            val registros = response.embedded?.registros ?: emptyList()

            registros.forEach { registro ->
                val fecha = LocalDate.parse(registro.fecha)
                weeklyCalories[fecha.dayOfWeek] = registro.totalCalorias
            }
            weeklyCalories
        } catch (e: Exception) {
            e.printStackTrace()
            weeklyCalories
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTodaysMeals(userId: Long): List<ComidaAlacenada> {
        val todaysMeals = mutableListOf<ComidaAlacenada>()
        val hoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        try {
            val comidasResponse = backendApiService.getComidasByUsuarioAndFecha(userId, hoy)
            val comidas = comidasResponse.embedded?.comidas ?: return emptyList()

            for (comida in comidas) {
                val comidaAlimentosResponse = backendApiService.getAlimentosByComida(comida.id)
                val comidaAlimentos = comidaAlimentosResponse.embedded?.comidaAlimentos ?: continue

                for (comidaAlimento in comidaAlimentos) {
                    val alimentoDto = backendApiService.getAlimentoById(comidaAlimento.alimentoId)
                    val alimento = Alimento(
                        id = alimentoDto.id,
                        nombre = alimentoDto.nombre,
                        caloriasPor100g = alimentoDto.calorias,
                        proteinasPor100g = alimentoDto.proteinas,
                        carbosPor100g = alimentoDto.carbos,
                        grasasPor100g = alimentoDto.grasas
                    )
                    todaysMeals.add(
                        ComidaAlacenada(
                            id = comidaAlimento.id,
                            alimento = alimento,
                            cantidadEnGramos = comidaAlimento.cantidadEnGramos,
                            tipoDeComida = comida.tipoDeComida
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return todaysMeals
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveFoodInMeal(
        userId: Long,
        alimento: Alimento,
        cantidad: Int,
        tipoComida: String
    ): Boolean {
        return try {
            val alimentoId = alimento.id ?: backendApiService.buscarAlimentosEnBackend(alimento.nombre)
                .embedded?.alimentos?.firstOrNull()?.id ?: return false

            val hoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val comidasDeHoy = backendApiService.getComidasByUsuarioAndFecha(userId, hoy)
            var comidaDeHoy = comidasDeHoy.embedded?.comidas?.firstOrNull { it.tipoDeComida.equals(tipoComida, ignoreCase = true) }

            if (comidaDeHoy == null) {
                val createRequest = ComidaCreateRequest(tipoDeComida = tipoComida.uppercase(), fecha = hoy, usuario = UsuarioIdWrapper(id = userId))
                comidaDeHoy = backendApiService.crearComida(createRequest)
            }

            val addAlimentoRequest = ComidaAlimentoRequest(
                comida = ComidaIdWrapper(id = comidaDeHoy.id),
                alimento = AlimentoIdWrapper(id = alimentoId),
                cantidadEnGramos = cantidad
            )
            backendApiService.agregarAlimentoAComida(addAlimentoRequest)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
