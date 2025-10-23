package com.example.nutriapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutriapp.R.drawable.nutrialogo
import com.example.nutriapp.ui.theme.Inter // Import Inter font

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Nueva paleta de colores
    val darkPurpleBackground = Color(0xFF130c27)
    val intermediatePurple = Color(0xFF2b2047)
    val lightPurpleButton = Color(0xFFaa8dff)
    val lightPurpleText = Color(0xFFa49add)
    val whiteText = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkPurpleBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = nutrialogo),
            contentDescription = "Login Image",
            modifier = Modifier.size(200.dp),
            colorFilter = ColorFilter.tint(lightPurpleButton) // Tint the logo
        )

        Text(
            text = "Bienvenido a NutriAPP", 
            fontFamily = Inter, 
            fontWeight = FontWeight.Bold, 
            fontSize = 28.sp, 
            color = whiteText
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Inicia tu sesion", fontFamily = Inter, color = lightPurpleText)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, 
            onValueChange = { email = it }, 
            label = { Text(text = "Tu email", fontFamily = Inter) },
            textStyle = TextStyle(fontFamily = Inter, color = whiteText),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lightPurpleButton,
                unfocusedBorderColor = intermediatePurple,
                focusedLabelColor = lightPurpleText,
                unfocusedLabelColor = lightPurpleText,
                cursorColor = whiteText
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text(text = "Tu contraseña", fontFamily = Inter) },
            textStyle = TextStyle(fontFamily = Inter, color = whiteText),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lightPurpleButton,
                unfocusedBorderColor = intermediatePurple,
                focusedLabelColor = lightPurpleText,
                unfocusedLabelColor = lightPurpleText,
                cursorColor = whiteText
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = lightPurpleButton)
            ) {
                Text(text = "Iniciar Sesion", fontFamily = Inter, fontWeight = FontWeight.Bold, color = darkPurpleBackground)
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los botones
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = lightPurpleButton)
            ) {
                Text(text = "Registrate", fontFamily = Inter, fontWeight = FontWeight.Bold, color = darkPurpleBackground)
            }
        }
    }
}
