package com.example.nutriapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nutriapp.ui.navigation.NavItem
import kotlinx.coroutines.delay

private val nutritionFacts = listOf(
    "Las zanahorias no siempre fueron naranjas. Originalmente eran moradas o amarillas.",
    "El brócoli contiene más proteína por caloría que un filete.",
    "Las manzanas flotan en el agua porque están compuestas por un 25% de aire.",
    "Los pimientos tienen más vitamina C que las naranjas.",
    "El pepino es técnicamente una fruta y está compuesto por un 96% de agua.",
    "Las almendras son miembros de la familia de los duraznos."
)

@Composable
fun TransicionLogin(navController: NavController, username: String) {
    var isLoading by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }
    val randomFact = remember { nutritionFacts.random() }

    // Lógica de la transición
    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
        delay(100)
        showContent = true
        delay(4000)


        navController.navigate(NavItem.Home.route) {
            popUpTo(NavItem.TransicionLogin.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¡Bienvenido, $username!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = randomFact,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
