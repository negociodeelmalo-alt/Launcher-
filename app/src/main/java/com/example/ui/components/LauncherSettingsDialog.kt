package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.preferences.LauncherConfig
import com.example.data.preferences.LauncherPreferences

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LauncherSettingsDialog(
    config: LauncherConfig,
    currentTime: String,
    currentDate: String,
    weatherTemp: String,
    onSetClockColor: (Long) -> Unit,
    onSetDateColor: (Long) -> Unit,
    onSetWeatherColor: (Long) -> Unit,
    onSetAccentColor: (Long) -> Unit,
    onSetIs24Hour: (Boolean) -> Unit,
    onSetShowSeconds: (Boolean) -> Unit,
    onSetGridColumns: (Int) -> Unit,
    onResetDefaults: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val accentColor = config.composeAccentColor

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .padding(horizontal = 48.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main Glass Modal Container
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(26.dp),
                accentColor = accentColor,
                elevation = 16.dp
            ) { _ ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Header row: Title + Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(accentColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = "Opciones",
                                    tint = accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Opciones del Launcher",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Personaliza colores de hora, fecha, tiempo y estilo liquid glass",
                                    style = TextStyle(
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        // Close button
                        LiquidGlassCard(
                            shape = RoundedCornerShape(12.dp),
                            accentColor = accentColor,
                            onClick = onDismiss,
                            elevation = 2.dp,
                            modifier = Modifier.testTag("close_settings_button")
                        ) { isFocused ->
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = if (isFocused) accentColor else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Listo",
                                    style = TextStyle(
                                        color = if (isFocused) accentColor else Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Preview Pod
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "VISTA PREVIA EN VIVO",
                                    style = TextStyle(
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = currentTime,
                                        style = TextStyle(
                                            color = config.composeClockColor,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = currentDate,
                                        style = TextStyle(
                                            color = config.composeDateColor,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            // Weather chip preview
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = "Clima",
                                    tint = config.composeWeatherColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = weatherTemp,
                                    style = TextStyle(
                                        color = config.composeWeatherColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Horizontal Navigation Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val tabs = listOf(
                            "Hora" to Icons.Default.AccessTime,
                            "Fecha" to Icons.Default.CalendarToday,
                            "Tiempo" to Icons.Default.WbSunny,
                            "Foco y Acento" to Icons.Default.Palette,
                            "Cuadrícula y Reloj" to Icons.Default.GridOn
                        )

                        tabs.forEachIndexed { index, (label, icon) ->
                            val isSelected = selectedSection == index
                            LiquidGlassCard(
                                shape = RoundedCornerShape(12.dp),
                                accentColor = accentColor,
                                onClick = { selectedSection = index },
                                elevation = if (isSelected) 4.dp else 1.dp
                            ) { isFocused ->
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (isSelected || isFocused) accentColor else Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = label,
                                        style = TextStyle(
                                            color = if (isSelected || isFocused) Color.White else Color.White.copy(alpha = 0.7f),
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Content (Scrollable for TV screens)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (selectedSection) {
                            0 -> ColorPickerSection(
                                title = "Color de la Hora",
                                description = "Selecciona el color brillante para el reloj digital principal",
                                currentColor = config.clockColor,
                                onSelectColor = onSetClockColor,
                                accentColor = accentColor
                            )
                            1 -> ColorPickerSection(
                                title = "Color de la Fecha",
                                description = "Selecciona el color para el texto del día de la semana y fecha",
                                currentColor = config.dateColor,
                                onSelectColor = onSetDateColor,
                                accentColor = accentColor
                            )
                            2 -> ColorPickerSection(
                                title = "Color del Tiempo y Clima",
                                description = "Selecciona el color del icono de clima y temperatura en vivo",
                                currentColor = config.weatherColor,
                                onSelectColor = onSetWeatherColor,
                                accentColor = accentColor
                            )
                            3 -> ColorPickerSection(
                                title = "Color de Foco y Acento D-Pad",
                                description = "Selecciona el color del halo de selección del control remoto",
                                currentColor = config.accentColor,
                                onSelectColor = onSetAccentColor,
                                accentColor = accentColor
                            )
                            4 -> GridAndOptionsSection(
                                config = config,
                                onSetIs24Hour = onSetIs24Hour,
                                onSetShowSeconds = onSetShowSeconds,
                                onSetGridColumns = onSetGridColumns,
                                onResetDefaults = onResetDefaults,
                                accentColor = accentColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorPickerSection(
    title: String,
    description: String,
    currentColor: Long,
    onSelectColor: (Long) -> Unit,
    accentColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = description,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Preset color pills in a wrapping row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LauncherPreferences.PRESET_COLORS.forEach { (colorVal, colorName) ->
                val isSelected = currentColor == colorVal
                val swatchColor = Color(colorVal)

                LiquidGlassCard(
                    shape = RoundedCornerShape(14.dp),
                    accentColor = accentColor,
                    onClick = { onSelectColor(colorVal) },
                    elevation = if (isSelected) 6.dp else 2.dp
                ) { isFocused ->
                    Row(
                        modifier = Modifier
                            .background(
                                if (isSelected) Color.White.copy(alpha = 0.22f) else Color.Transparent
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(swatchColor)
                                .border(
                                    2.dp,
                                    if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (colorVal == 0xFFFFFFFF) Color.Black else Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = colorName,
                            style = TextStyle(
                                color = if (isSelected || isFocused) Color.White else Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GridAndOptionsSection(
    config: LauncherConfig,
    onSetIs24Hour: (Boolean) -> Unit,
    onSetShowSeconds: (Boolean) -> Unit,
    onSetGridColumns: (Int) -> Unit,
    onResetDefaults: () -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Grid Columns selector
        Column {
            Text(
                text = "Columnas de la cuadrícula de aplicaciones",
                style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(4, 5, 6, 7).forEach { cols ->
                    val isSelected = config.gridColumns == cols
                    LiquidGlassCard(
                        shape = RoundedCornerShape(12.dp),
                        accentColor = accentColor,
                        onClick = { onSetGridColumns(cols) },
                        elevation = if (isSelected) 4.dp else 1.dp
                    ) { isFocused ->
                        Box(
                            modifier = Modifier
                                .background(if (isSelected) accentColor.copy(alpha = 0.25f) else Color.Transparent)
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cols columnas",
                                style = TextStyle(
                                    color = if (isSelected) accentColor else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }

        // 24 Hour Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Formato 24 Horas",
                    style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Alterna entre 24h (14:30) y 12h (02:30 PM)",
                    style = TextStyle(color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
                )
            }
            Switch(
                checked = config.is24Hour,
                onCheckedChange = onSetIs24Hour,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor,
                    checkedTrackColor = accentColor.copy(alpha = 0.4f)
                )
            )
        }

        // Show Seconds Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Mostrar Segundos en Reloj",
                    style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Actualización precisa segundo a segundo",
                    style = TextStyle(color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
                )
            }
            Switch(
                checked = config.showSeconds,
                onCheckedChange = onSetShowSeconds,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor,
                    checkedTrackColor = accentColor.copy(alpha = 0.4f)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Reset to Defaults Button
        LiquidGlassCard(
            shape = RoundedCornerShape(12.dp),
            accentColor = Color(0xFFFF5252),
            onClick = onResetDefaults,
            elevation = 2.dp
        ) { isFocused ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restablecer",
                    tint = if (isFocused) Color(0xFFFF5252) else Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Restablecer a Colores Predeterminados",
                    style = TextStyle(
                        color = if (isFocused) Color(0xFFFF5252) else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
