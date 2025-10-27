package com.example.nutriapp.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    LaunchedEffect(uiState.loginStatus) {
        if (uiState.loginStatus == LoginStatus.SUCCESS) {
            val user = uiState.loggedInUser
            if (user != null) {
                navController.navigate(NavItem.TransicionLogin.route + "/${user.username}") {
                    popUpTo(NavItem.Login.route) { inclusive = true }
                }
                // Resetea el estado después de navegar para evitar bucles
                loginViewModel.onNavigationHandled()
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
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000)) + fadeIn(animationSpec = tween(1000))) {
                Image(
                    painter = painterResource(id = R.drawable.nutrialogo),
                    contentDescription = "Login Image",
                    modifier = Modifier.size(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 200)) + fadeIn(tween(1000, 200))) {
                Text(
                    text = "Bienvenido a NutriAPP",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 400)) + fadeIn(tween(1000, 400))) {
                Text(
                    text = "Inicia tu sesion",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (uiState.loginStatus == LoginStatus.ERROR) {
                Text(
                    text = "Usuario o contraseña incorrectos",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 600)) + fadeIn(tween(1000, 600))) {
                OutlinedTextField(
                    value = uiState.usernameOrEmail,
                    onValueChange = loginViewModel::onUsernameOrEmailChange,
                    label = { Text(text = "Email o Usuario") },
                    isError = uiState.loginStatus == LoginStatus.ERROR,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 800)) + fadeIn(tween(1000, 800))) {
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
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 1000)) + fadeIn(tween(1000, 1000))) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = loginViewModel::login,
                        enabled = uiState.loginStatus != LoginStatus.LOADING,
                        modifier = Modifier.weight(1f)
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Registrate")
                    }
                }
            }
        }
    }
}
