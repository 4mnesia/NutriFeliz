package com.example.nutriapp.repository

import android.os.Build
import android.util.Log
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

    suspend fun buscarAlimento(query: String): Alimento? {
        return try {
            val response = backendApiService.buscarAlimentosEnBackend(query)
            response.embedded?.alimentos?.firstOrNull()?.let {
                Alimento(id = it.id, nombre = it.nombre, caloriasPor100g = it.calorias, proteinasPor100g = it.proteinas, carbosPor100g = it.carbos, grasasPor100g = it.grasas)
            } ?: buscarAlimentoExterno(query)
        } catch (e: Exception) {
            e.printStackTrace()
            buscarAlimentoExterno(query)
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

    suspend fun buscarSugerencias(query: String): List<Alimento> {
        return try {
            val backendResponse = backendApiService.buscarAlimentosEnBackend(query)
            val backendAlimentos = backendResponse.embedded?.alimentos?.map {
                Alimento(id = it.id, nombre = it.nombre, caloriasPor100g = it.calorias, proteinasPor100g = it.proteinas, carbosPor100g = it.carbos, grasasPor100g = it.grasas)
            }

            if (!backendAlimentos.isNullOrEmpty()) {
                return backendAlimentos
            }

            externalApiService.getFoodDetails(query).items.map { it.toAlimento() }

        } catch (e: Exception) {
            e.printStackTrace()
            try {
                externalApiService.getFoodDetails(query).items.map { it.toAlimento() }
            } catch (e2: Exception) {
                e2.printStackTrace()
                emptyList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getWeeklyCalories(userId: Long?): Map<DayOfWeek, Int> {
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
    suspend fun getTodaysMeals(userId: Long?): List<ComidaAlacenada> {
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
    suspend fun guardarAlimentoEnBackend(
        userId: Long?,
        alimento: Alimento,
        cantidadIngresada: Int,
        tipoComida: String
    ): Boolean {
        return try {
            // PASO 1: Obtener el ID del alimento en TU backend.
            var alimentoIdFinal = alimento.id

            // Si el ID es null o 0, asumimos que es nuevo o externo.
            if (alimentoIdFinal == null || alimentoIdFinal == 0L) {
                try {
                    // A. Intentamos buscarlo por nombre exacto para evitar duplicados
                    val busqueda = backendApiService.buscarAlimentosEnBackend(alimento.nombre)
                    val alimentoExistente = busqueda.embedded?.alimentos?.firstOrNull {
                        it.nombre.equals(alimento.nombre, ignoreCase = true)
                    }

                    if (alimentoExistente != null) {
                        alimentoIdFinal = alimentoExistente.id
                    } else {
                        // B. Si no existe, lo CREAMOS en tu backend
                        val nuevoAlimentoRequest = AlimentoCreateRequest(
                            nombre = alimento.nombre,
                            calorias = alimento.caloriasPor100g,
                            proteinas = alimento.proteinasPor100g,
                            carbos = alimento.carbosPor100g,
                            grasas = alimento.grasasPor100g
                        )
                        val alimentoCreado = backendApiService.crearAlimento(nuevoAlimentoRequest)
                        alimentoIdFinal = alimentoCreado.id
                    }
                } catch (e: Exception) {
                    Log.e("FoodRepository", "Error creando/buscando alimento: ${e.message}")
                    return false // Si falla la gestión del alimento, no podemos seguir
                }
            }

            // VALIDACIÓN DE SEGURIDAD: Si después de todo sigue siendo null, abortamos.
            // Esto evita que la app explote en el Paso 3.
            val finalId = alimentoIdFinal ?: run {
                Log.e("FoodRepository", "Error: ID de alimento es null. No se puede guardar.")
                return false
            }

            // PASO 2: Obtener o Crear la "Comida" (Cabecera: Desayuno/Almuerzo de hoy)
            val hoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val comidasResponse = backendApiService.getComidasByUsuarioAndFecha(userId, hoy)

            // Buscamos si ya existe ese tiempo de comida (ej. DESAYUNO)
            var comidaTarget = comidasResponse.embedded?.comidas?.find {
                it.tipoDeComida.equals(tipoComida, ignoreCase = true)
            }

            if (comidaTarget == null) {
                val nuevaComidaRequest = ComidaCreateRequest(
                    tipoDeComida = tipoComida.uppercase(),
                    fecha = hoy,
                    usuario = UsuarioIdWrapper(id = userId)
                )
                comidaTarget = backendApiService.crearComida(nuevaComidaRequest)
            }

            // PASO 3: Crear la relación (Comida - Alimento)
            // Usamos 'finalId' que ya sabemos que NO es nulo
            val relacionRequest = ComidaAlimentoRequest(
                comida = ComidaIdWrapper(id = comidaTarget.id),
                alimento = AlimentoIdWrapper(id = finalId),
                cantidadEnGramos = cantidadIngresada
            )

            backendApiService.agregarAlimentoAComida(relacionRequest)
            Log.d("FoodRepository", "Alimento guardado correctamente en backend.")
            true

        } catch (e: Exception) {
            Log.e("FoodRepository", "Excepción al guardar en backend: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun buscarEnApiExterna(query: String): List<Alimento> {
        return try {
            // Llamamos al servicio configurado para la API externa (ej. CalorieNinjas)
            val response = externalApiService.getFoodDetails(query)

            // Mapeamos la respuesta (CNItem) a tu modelo de UI (Alimento)
            // usando la función de extensión .toAlimento() que creamos antes
            response.items.map { it.toAlimento() }

        } catch (e: Exception) {
            Log.e("FoodRepository", "Error buscando en API externa: ${e.message}")
            // En caso de error, devolvemos una lista vacía para no romper la UI
            emptyList()
        }
    }
}
