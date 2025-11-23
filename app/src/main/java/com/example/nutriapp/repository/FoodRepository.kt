package com.example.nutriapp.repository

import com.example.nutriapp.model.home.Alimento
import com.example.nutriapp.model.toAlimento
import com.example.nutriapp.remote.RetrofitInstance

class FoodRepository {
    private val apiService = RetrofitInstance.apiServiceFood

    suspend fun buscarAlimento(query: String): Alimento? {
        return try {
            val response = apiService.getFoodDetails(query)
            response.items.firstOrNull()?.toAlimento()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun buscarSugerencias(query: String): List<Alimento> {
        return try {
            val response = apiService.getFoodDetails(query)
            response.items.take(5).map { it.toAlimento() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}