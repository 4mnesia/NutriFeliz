package com.example.nutriapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nutriapp.ui.navigation.NavItem
import com.example.nutriapp.util.PasswordStrength
import com.example.nutriapp.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    val uiState by registrationViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            navController.navigate(NavItem.Login.route) {
                popUpTo(NavItem.Login.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = startAnimation, enter = fadeIn(tween(1000))) {
                    Text(
                        text = "Crea tu cuenta",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 200)) + fadeIn(tween(1000, 200))) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.fullName,
                        onValueChange = registrationViewModel::onFullNameChange,
                        label = { Text(text = "Nombre Completo") },
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 400)) + fadeIn(tween(1000, 400))) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.username,
                        onValueChange = registrationViewModel::onUsernameChange,
                        label = { Text(text = "Nombre de Usuario") },
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 600)) + fadeIn(tween(1000, 600))) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.email,
                        onValueChange = registrationViewModel::onEmailChange,
                        label = { Text(text = "Tu email") },
                        isError = !uiState.isEmailValid && uiState.email.isNotEmpty(),
                        supportingText = { if (!uiState.isEmailValid && uiState.email.isNotEmpty()) Text("Formato de email no válido") },
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 800)) + fadeIn(tween(1000, 800))) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.password,
                        onValueChange = registrationViewModel::onPasswordChange,
                        label = { Text(text = "Contraseña") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        }
                    )
                }
                PasswordStrengthIndicator(strength = uiState.passwordStrength)
                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 1000)) + fadeIn(tween(1000, 1000))) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.confirmPassword,
                        onValueChange = registrationViewModel::onConfirmPasswordChange,
                        label = { Text(text = "Confirmar Contraseña") },
                        isError = !uiState.passwordsMatch && uiState.confirmPassword.isNotEmpty(),
                        supportingText = { if (!uiState.passwordsMatch && uiState.confirmPassword.isNotEmpty()) Text("Las contraseñas no coinciden") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(visible = startAnimation, enter = slideInVertically(animationSpec = tween(1000, 1200)) + fadeIn(tween(1000, 1200))) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = registrationViewModel::registerUser,
                        enabled = uiState.isFormValid
                    ) {
                        Text(text = "Registrarse")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(visible = startAnimation, enter = fadeIn(tween(1000, 1400))) {
                    Text(
                        text = "¿Ya tienes una cuenta? Inicia sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
