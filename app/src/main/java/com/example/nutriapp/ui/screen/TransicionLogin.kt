package com.example.nutriapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nutriapp.R
import com.example.nutriapp.navigation.NavItem
import kotlinx.coroutines.delay

// Lista de datos curiosos sobre nutrición para mostrar en la pantalla de transición.
private val nutritionFacts = listOf(
    "Las zanahorias no siempre fueron naranjas. Originalmente eran moradas o amarillas.",
    "El brócoli contiene más proteína por caloría que un filete.",
    "Las manzanas flotan en el agua porque están compuestas por un 25% de aire.",
    "Los pimientos tienen más vitamina C que las naranjas.",
    "El pepino es técnicamente una fruta y está compuesto por un 96% de agua.",
    "Las almendras son miembros de la familia de los duraznos.",
    "El aguacate es una fruta, no una verdura, y está lleno de grasas saludables.",
    "Los plátanos son bayas, pero las fresas no lo son.",
    "El chocolate negro es rico en antioxidantes y puede mejorar la salud del corazón.",
    "La miel nunca se echa a perder. Se ha encontrado miel comestible en tumbas egipcias de miles de años.",
    "La chía es una de las fuentes vegetales más ricas en ácidos grasos omega-3.",
    "Las palomitas de maíz son un grano integral.",
    "Los kiwis contienen casi el doble de vitamina C que las naranjas.",
    "La quinoa es uno de los pocos alimentos vegetales que son una proteína completa."
)

/**
 * Composable que representa una pantalla de transición animada después de un inicio de sesión exitoso.
 * Muestra una bienvenida, un dato curioso y luego navega a la pantalla principal.
 *
 * @param navController Controlador de navegación para redirigir al usuario a la pantalla de inicio.
 * @param username El nombre del usuario que se mostrará en el mensaje de bienvenida.
 */
@Composable
fun TransicionLogin(navController: NavController, username: String) {
    var isLoading by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }
    val randomFact = remember { nutritionFacts.random() } // Elige un dato curioso al azar
    
    var textLength by remember { mutableIntStateOf(0) } // Controla la longitud del texto para el efecto de máquina de escribir
    var startProgress by remember { mutableStateOf(false) } // Inicia la barra de progreso

    // Animación de pulso para la imagen de la nutria
    val infiniteTransition = rememberInfiniteTransition(label = "NutriaPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "NutriaScale"
    )

    // Orquesta la secuencia de animaciones y la navegación
    LaunchedEffect(Unit) {
        delay(2000) // Muestra el indicador de carga inicial
        isLoading = false
        showContent = true

        // Anima el efecto de máquina de escribir para el dato curioso
        val typewriterDuration = randomFact.length * 50
        animate(0f, randomFact.length.toFloat(), animationSpec = tween(typewriterDuration)) {
            value, _ -> textLength = value.toInt()
        }

        // Inicia la barra de progreso y espera antes de navegar
        startProgress = true
        delay(2000)

        // Navega a la pantalla de inicio y limpia la pila de navegación
        navController.navigate(NavItem.Home.route + "/$username") {
            popUpTo(NavItem.Login.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Muestra un indicador de carga al principio
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.Center))
        }

        // Contenido principal que aparece después de la carga inicial
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp),
            visible = showContent,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
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
                    text = randomFact.substring(0, textLength),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.height(80.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.nutriaidea),
                    contentDescription = "Nutria Idea",
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }

        // Barra de progreso lineal en la parte superior
        val animatedProgress by animateFloatAsState(
            targetValue = if (startProgress) 1f else 0f, 
            label = "TransitionProgress", 
            animationSpec = tween(2000)
        )
        
        if(showContent) {
             LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background
            )
        }
    }
}
