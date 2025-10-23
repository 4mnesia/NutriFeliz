package com.example.nutriapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nutriapp.R
import com.example.nutriapp.ui.navigation.NavItem
import com.example.nutriapp.viewmodel.LoginStatus
import com.example.nutriapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Observa el estado del login para navegar cuando sea exitoso
    LaunchedEffect(uiState.loginStatus) {
        if (uiState.loginStatus == LoginStatus.SUCCESS) {
            val user = uiState.loggedInUser
            if (user != null) {
                // Navega a la pantalla de transición, no a la de Home directamente
                navController.navigate(NavItem.TransicionLogin.route + "/${user.username}") {
                    popUpTo(NavItem.Login.route) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                    slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(durationMillis = 1000))
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nutrialogo),
                    contentDescription = "Login Image",
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    text = "Bienvenido a NutriAPP",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Inicia tu sesion",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (uiState.loginStatus == LoginStatus.SUCCESS || uiState.loginStatus == LoginStatus.ERROR) {
                    val message = if (uiState.loginStatus == LoginStatus.SUCCESS) "¡Inicio de sesión exitoso!" else "Usuario o contraseña incorrectos"
                    val color = if (uiState.loginStatus == LoginStatus.SUCCESS) Color(0xFF00C853) else MaterialTheme.colorScheme.error
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        color = color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.usernameOrEmail,
                    onValueChange = loginViewModel::onUsernameOrEmailChange,
                    label = { Text(text = "Email o Usuario") },
                    isError = uiState.loginStatus == LoginStatus.ERROR
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = loginViewModel::onPasswordChange,
                    label = { Text(text = "Contraseña") },
                    isError = uiState.loginStatus == LoginStatus.ERROR,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = loginViewModel::login,
                        enabled = uiState.loginStatus != LoginStatus.LOADING
                    ) {
                        if (uiState.loginStatus == LoginStatus.LOADING) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(text = "Iniciar Sesion")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { navController.navigate(NavItem.Registration.route) },
                    ) {
                        Text(text = "Registrate")
                    }
                }
            }
        }
    }
}
