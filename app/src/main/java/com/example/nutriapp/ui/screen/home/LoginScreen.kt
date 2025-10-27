package com.example.nutriapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.R.drawable.nutrialogo

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Image(
            painter = painterResource(id = nutrialogo),
            contentDescription = "Login Image",
            modifier = Modifier.size(200.dp)
        )

        Text(text = "Bienvenido a NutriAPP", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Inicia tu sesion")

        OutlinedTextField(value = "", onValueChange = {}, label = {
            Text(text = "Tu email")
        })
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = {
            Text(text = "Tu contraseña")
        })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { }) {
            Text(text = "Iniciar Sesion")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { }) {
            Text(text = "Registrate")
        }

    }
}





