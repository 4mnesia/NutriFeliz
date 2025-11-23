package com.example.nutriapp.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val apiServiceFood: ApiServiceFood by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceFood::class.java)
    }
}