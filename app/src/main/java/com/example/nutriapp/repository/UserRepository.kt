package com.example.nutriapp.repository

import com.example.nutriapp.model.User
import com.example.nutriapp.network.ApiService
import com.example.nutriapp.network.LoginRequest
import com.example.nutriapp.network.NuevoPesoRequest
import com.example.nutriapp.network.RegistrationRequest
import com.example.nutriapp.network.UsuarioDTO
import com.example.nutriapp.network.UsuarioUpdateRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

// Resultado sellado para un manejo de errores más robusto
sealed class RegistrationResult {
    object Success : RegistrationResult()
    data class Error(val message: String) : RegistrationResult()
}

@Singleton
class UserRepository @Inject constructor(
    private val backendApiService: ApiService
) {

    // ... (otras funciones como getWeightHistory, etc. no cambian)
    suspend fun getWeightHistory(userId: Long?): List<Float> {
        return try {
            val response = backendApiService.getHistorialPeso(userId)
            response.embedded?.historial?.map { it.peso.toFloat() } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveNewWeight(userId: Long?, weight: Double): Boolean {
        return try {
            val request = NuevoPesoRequest(peso = weight)
            backendApiService.addPesoEntry(userId, request)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getUserData(userId: Long?): UsuarioDTO? {
        return try {
            backendApiService.getUsuarioById(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserGoals(userId: Long?, calories: Int, protein: Int, carbs: Int, fat: Int): Boolean {
        return try {
            val request = UsuarioUpdateRequest(metaCalorias = calories, metaProteinas = protein, metaCarbos = carbs, metaGrasas = fat)
            backendApiService.updateUsuario(userId, request)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // --- FUNCIÓN DE REGISTRO CORREGIDA ---
    suspend fun addUser(user: User): RegistrationResult {
        return try {
            val request = RegistrationRequest(
                fullName = user.fullName,       // Usa fullName
                username = user.username,
                email = user.email,
                passwordHash = user.passwordHash  // Usa passwordHash
            )
            val response: Response<Void> = backendApiService.registerUser(request)
            if (response.isSuccessful) {
                RegistrationResult.Success
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error de registro desconocido"
                RegistrationResult.Error(errorBody)
            }
        } catch (e: Exception) {
            RegistrationResult.Error(e.message ?: "Error de conexión")
        }
    }

    // --- FUNCIÓN DE LOGIN CORREGIDA ---
    suspend fun findUser(usernameOrEmail: String, passwordAttempt: String): UsuarioDTO? {
        return try {
            val request = LoginRequest(usernameOrEmail, passwordAttempt)
            backendApiService.loginUser(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
