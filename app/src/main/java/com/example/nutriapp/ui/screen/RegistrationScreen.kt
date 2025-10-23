package com.example.nutriapp.ui.screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.example.nutriapp.data.User
import com.example.nutriapp.data.UserRepository
import com.example.nutriapp.ui.navigation.NavItem
import com.example.nutriapp.util.PasswordStrength
import com.example.nutriapp.util.calculatePasswordStrength
import com.example.nutriapp.util.isEmailValid

@Composable
fun RegistrationScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var isEmailTouched by remember { mutableStateOf(false) }
    var emailIsValid by remember { mutableStateOf(false) }
    var passwordStrength by remember { mutableStateOf(PasswordStrength.WEAK) }
    var passwordsMatch by remember { mutableStateOf(true) }

    LaunchedEffect(email) {
        if (isEmailTouched) {
            emailIsValid = isEmailValid(email)
        }
    }

    LaunchedEffect(password) {
        passwordStrength = calculatePasswordStrength(password)
        passwordsMatch = password.isNotEmpty() && password == confirmPassword
    }

    LaunchedEffect(confirmPassword) {
        passwordsMatch = password == confirmPassword
    }

    val isFormValid = emailIsValid && passwordsMatch && password.isNotEmpty() && fullName.isNotEmpty() && username.isNotEmpty() && passwordStrength != PasswordStrength.WEAK

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
                Text(
                    text = "Crea tu cuenta",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(text = "Nombre Completo") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = "Nombre de Usuario") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it; isEmailTouched = true },
                    label = { Text(text = "Tu email") },
                    isError = !emailIsValid && isEmailTouched && email.isNotEmpty(),
                    supportingText = { if (!emailIsValid && isEmailTouched && email.isNotEmpty()) Text("Formato de email no válido") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility")
                        }
                    }
                )
                PasswordStrengthIndicator(strength = passwordStrength)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(text = "Confirmar Contraseña") },
                    isError = !passwordsMatch && confirmPassword.isNotEmpty(),
                    supportingText = { if (!passwordsMatch && confirmPassword.isNotEmpty()) Text("Las contraseñas no coinciden") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val newUser = User(fullName, username, email, password)
                        UserRepository.addUser(newUser)
                        Log.d("Registration", "User registered: $newUser")
                        Log.d("Registration", "Current users: ${UserRepository.users}")
                        navController.navigate(NavItem.Login.route) {
                            popUpTo(NavItem.Login.route) { inclusive = true }
                        }
                    },
                    enabled = isFormValid
                ) {
                    Text(text = "Registrarse")
                }
                Spacer(modifier = Modifier.height(16.dp))
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

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {
    val (targetColor, targetProgress) = when (strength) {
        PasswordStrength.WEAK -> Pair(Color.Red, 0.25f)
        PasswordStrength.MEDIUM -> Pair(Color.Yellow, 0.5f)
        PasswordStrength.STRONG -> Pair(Color(0xFF00F59B), 0.75f)
        PasswordStrength.VERY_STRONG -> Pair(Color.Green, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500),
        label = "Password Strength Progress"
    )
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "Password Strength Color"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = animatedColor,
            trackColor = MaterialTheme.colorScheme.background
        )
    }
}
