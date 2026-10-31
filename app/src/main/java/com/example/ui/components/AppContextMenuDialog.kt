package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.AppInfo

@Composable
fun AppContextMenuDialog(
    app: AppInfo,
    accentColor: Color,
    onLaunch: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenDetails: () -> Unit,
    onUninstall: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f)),
            contentAlignment = Alignment.Center
        ) {
            LiquidGlassCard(
                modifier = Modifier.widthIn(min = 340.dp, max = 420.dp),
                shape = RoundedCornerShape(24.dp),
                accentColor = accentColor,
                elevation = 16.dp
            ) { _ ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Icon + Title
                    if (app.icon != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = app.icon),
                            contentDescription = app.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(
                        text = app.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = app.packageName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Menu Options
                    MenuItem(
                        title = "Abrir aplicación",
                        icon = Icons.Default.PlayArrow,
                        accentColor = accentColor,
                        onClick = onLaunch
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MenuItem(
                        title = if (app.isFavorite) "Quitar de Favoritas" else "Fijar en Favoritas",
                        icon = if (app.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        accentColor = Color(0xFFFFD600),
                        onClick = onToggleFavorite
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MenuItem(
                        title = "Información de la aplicación",
                        icon = Icons.Default.Info,
                        accentColor = accentColor,
                        onClick = onOpenDetails
                    )

                    if (!app.isSystemApp) {
                        Spacer(modifier = Modifier.height(8.dp))
                        MenuItem(
                            title = "Desinstalar aplicación",
                            icon = Icons.Default.Delete,
                            accentColor = Color(0xFFFF5252),
                            onClick = onUninstall
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cancel
                    LiquidGlassCard(
                        shape = RoundedCornerShape(12.dp),
                        accentColor = Color.White,
                        onClick = onDismiss,
                        elevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { isFocused ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Volver",
                                style = TextStyle(
                                    color = if (isFocused) accentColor else Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    LiquidGlassCard(
        shape = RoundedCornerShape(12.dp),
        accentColor = accentColor,
        onClick = onClick,
        elevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) { isFocused ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isFocused) accentColor else Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = TextStyle(
                    color = if (isFocused) accentColor else Color.White,
                    fontSize = 14.sp,
                    fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}
