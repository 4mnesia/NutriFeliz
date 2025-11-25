package com.example.nutriapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NutriAppApplication : Application() {
    // El cuerpo puede estar vacío. La anotación es lo que importa.
}
