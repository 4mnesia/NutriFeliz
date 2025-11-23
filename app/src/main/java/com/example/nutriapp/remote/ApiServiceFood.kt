package com.example.nutriapp.remote
import com.example.nutriapp.model.CNResponse
import retrofit2.http.*


interface ApiServiceFood {
    @Headers("x-api-key: XB/kxFzmkStmr1Rwx9+eug==VA0sDWcPpGtXj09M")
    @GET("v1/nutrition")
    suspend fun getFoodDetails(
        @Query("query") query: String
    ):CNResponse
}