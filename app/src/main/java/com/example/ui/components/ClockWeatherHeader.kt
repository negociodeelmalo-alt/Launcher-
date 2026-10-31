package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.LauncherConfig

@Composable
fun ClockWeatherHeader(
    currentTime: String,
    currentDate: String,
    weatherTemp: String,
    weatherLocation: String,
    config: LauncherConfig,
    onOpenSettings: () -> Unit,
    onOpenSystemSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clockColor = config.composeClockColor
    val dateColor = config.composeDateColor
    val weatherColor = config.composeWeatherColor
    val accentColor = config.composeAccentColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Big Time & Date
        Column(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTime,
                style = TextStyle(
                    color = clockColor,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.65f),
                        blurRadius = 14f
                    )
                ),
                modifier = Modifier.testTag("clock_text")
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = currentDate,
                style = TextStyle(
                    color = dateColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.7f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier.testTag("date_text")
            )
        }

        // Right Row: Weather Pod & Action Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Liquid Glass Weather Capsule
            LiquidGlassCard(
                shape = RoundedCornerShape(16.dp),
                accentColor = weatherColor,
                onClick = onOpenSettings,
                elevation = 4.dp
            ) { isFocused ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Clima",
                        tint = weatherColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = weatherTemp,
                            style = TextStyle(
                                color = weatherColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = weatherLocation,
                            style = TextStyle(
                                color = dateColor.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }

            // Launcher Customization Options Button
            LiquidGlassCard(
                shape = RoundedCornerShape(16.dp),
                accentColor = accentColor,
                onClick = onOpenSettings,
                elevation = 4.dp,
                modifier = Modifier.testTag("launcher_settings_button")
            ) { isFocused ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Opciones del Launcher",
                        tint = if (isFocused) accentColor else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Personalizar",
                        style = TextStyle(
                            color = if (isFocused) accentColor else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // TV System Settings Button
            LiquidGlassCard(
                shape = RoundedCornerShape(16.dp),
                accentColor = accentColor,
                onClick = onOpenSystemSettings,
                elevation = 4.dp,
                modifier = Modifier.testTag("system_settings_button")
            ) { isFocused ->
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes del Sistema",
                        tint = if (isFocused) accentColor else Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
