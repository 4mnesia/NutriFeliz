package com.example.nutriapp.di

import com.example.nutriapp.network.ApiService
import com.example.nutriapp.remote.ApiServiceFood
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer dependencias a nivel de aplicación.
 * Este módulo le enseña a Hilt cómo crear instancias de objetos que no puede construir por sí mismo,
 * como las interfaces de Retrofit.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provee la instancia base de Retrofit para conectarse al backend propio.
     * La anotación `@Named` se usa para diferenciar esta instancia de Retrofit de otras.
     * @return Una instancia de Retrofit configurada para el backend.
     */
    @Provides
    @Singleton
    @Named("BackendRetrofit")
    fun provideBackendRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // URL base del backend local
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provee la implementación de la interfaz [ApiService] para el backend.
     * Hilt usará esta función para inyectar [ApiService] donde sea necesario.
     * @param retrofit La instancia de Retrofit para el backend, provista por [provideBackendRetrofit].
     * @return Una implementación de la interfaz [ApiService].
     */
    @Provides
    @Singleton
    fun provideBackendApiService(@Named("BackendRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    /**
     * Provee la instancia base de Retrofit para conectarse a la API externa de alimentos (CalorieNinjas).
     * La anotación `@Named` es crucial para que Hilt no la confunda con la del backend.
     * @return Una instancia de Retrofit configurada para la API externa.
     */
    @Provides
    @Singleton
    @Named("ExternalFoodRetrofit")
    fun provideExternalFoodRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/") // URL base de la API de CalorieNinjas
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provee la implementación de la interfaz [ApiServiceFood] para la API externa.
     * @param retrofit La instancia de Retrofit para la API externa, provista por [provideExternalFoodRetrofit].
     * @return Una implementación de la interfaz [ApiServiceFood].
     */
    @Provides
    @Singleton
    fun provideExternalFoodApiService(@Named("ExternalFoodRetrofit") retrofit: Retrofit): ApiServiceFood {
        return retrofit.create(ApiServiceFood::class.java)
    }
}
