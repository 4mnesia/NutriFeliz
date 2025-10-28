package com.example.nutriapp.ui.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.nutriapp.notification.AlarmScheduler
import com.example.nutriapp.notification.NotificationPreferences
import com.example.nutriapp.ui.theme.ColorProfile
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    colorProfile: ColorProfile,
    setColorProfile: (ColorProfile) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { NotificationPreferences(context) }
    val alarmScheduler = remember { AlarmScheduler(context) }

    var expanded by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showExactAlarmDialog by remember { mutableStateOf(false) }

    var notificationsEnabled by remember { mutableStateOf(prefs.areNotificationsEnabled()) }
    var selectedHour by remember { mutableStateOf(prefs.getHour()) }
    var selectedMinute by remember { mutableStateOf(prefs.getMinute()) }
    var countdownText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = notificationsEnabled, key2 = selectedHour, key3 = selectedMinute) {
        if (notificationsEnabled) {
            while (true) {
                val now = Calendar.getInstance()
                val nextAlarm = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)

                    if (before(now)) {
                        add(Calendar.DATE, 1)
                    }
                }

                val diff = nextAlarm.timeInMillis - now.timeInMillis
                val hours = diff / (1000 * 60 * 60)
                val minutes = (diff / (1000 * 60)) % 60
                val seconds = (diff / 1000) % 60

                countdownText = String.format("Próximo recordatorio en: %02d:%02d:%02d", hours, minutes, seconds)
                delay(1000)
            }
        } else {
            countdownText = ""
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                notificationsEnabled = true
                prefs.setNotificationsEnabled(true)
                alarmScheduler.schedule(selectedHour, selectedMinute)
            } else {
                notificationsEnabled = false
            }
        }
    )

    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute,
        is24Hour = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Sección de Temas
            Text("Temas", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (colorProfile == ColorProfile.PREDETERMINADO) "Predeterminado" else "Rosa", color = MaterialTheme.colorScheme.onBackground)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Predeterminado", color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            setColorProfile(ColorProfile.PREDETERMINADO)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Rosa", color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            setColorProfile(ColorProfile.ROSA)
                            expanded = false
                        }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Sección de Notificaciones
            Text("Notificaciones", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Activar recordatorios", color = MaterialTheme.colorScheme.onBackground)
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmScheduler.canScheduleExactAlarms()) {
                                showExactAlarmDialog = true
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                when (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)) {
                                    PackageManager.PERMISSION_GRANTED -> {
                                        notificationsEnabled = true
                                        prefs.setNotificationsEnabled(true)
                                        alarmScheduler.schedule(selectedHour, selectedMinute)
                                    }
                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                }
                            } else {
                                notificationsEnabled = true
                                prefs.setNotificationsEnabled(true)
                                alarmScheduler.schedule(selectedHour, selectedMinute)
                            }
                        } else {
                            notificationsEnabled = false
                            prefs.setNotificationsEnabled(false)
                            alarmScheduler.cancel()
                        }
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = notificationsEnabled) { showTimePicker = true }
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hora del recordatorio",
                    color = if (notificationsEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
                Text(
                    text = String.format("%02d:%02d", selectedHour, selectedMinute),
                    color = if (notificationsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
            if (notificationsEnabled && countdownText.isNotEmpty()) {
                Text(
                    text = countdownText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Seleccionar hora") },
            text = {
                   TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        prefs.setNotificationTime(selectedHour, selectedMinute)
                        if (notificationsEnabled) {
                           alarmScheduler.schedule(selectedHour, selectedMinute)
                        }
                        showTimePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showExactAlarmDialog) {
        AlertDialog(
            onDismissRequest = { showExactAlarmDialog = false },
            title = { Text("Permiso Requerido") },
            text = { Text("Para asegurar que los recordatorios lleguen a tiempo, la aplicación necesita permiso para programar alarmas exactas. Por favor, activa el permiso en la siguiente pantalla.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExactAlarmDialog = false
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also {
                            context.startActivity(it)
                        }
                    }
                ) {
                    Text("Ir a Ajustes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExactAlarmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
