package com.example.nutriapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nutriapp.ui.screen.LoginScreen
import com.example.nutriapp.ui.navegation.NavegacionApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavegacionApp()



                }
            }
        }