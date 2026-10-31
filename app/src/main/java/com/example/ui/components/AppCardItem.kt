package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.AppInfo

@Composable
fun AppCardItem(
    app: AppInfo,
    accentColor: Color,
    focusRequester: FocusRequester? = null,
    onLaunch: () -> Unit,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier
            .aspectRatio(0.92f)
            .testTag("app_item_${app.packageName}"),
        shape = RoundedCornerShape(20.dp),
        accentColor = accentColor,
        focusRequester = focusRequester,
        onClick = onLaunch,
        onLongClick = onOpenMenu,
        elevation = 6.dp
    ) { isFocused ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Favorite Star Indicator
            if (app.isFavorite) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD600).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorita",
                        tint = Color(0xFFFFD600),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.15f))

                // App Icon with glass pod background
                Box(
                    modifier = Modifier
                        .size(if (isFocused) 64.dp else 56.dp)
                        .shadow(
                            elevation = if (isFocused) 10.dp else 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = if (isFocused) accentColor else Color.Black.copy(alpha = 0.5f)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = if (isFocused) 0.22f else 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (app.icon != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = app.icon),
                            contentDescription = app.name,
                            modifier = Modifier
                                .size(if (isFocused) 52.dp else 46.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Android,
                            contentDescription = app.name,
                            tint = if (isFocused) accentColor else Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // App Name with high-contrast shadow for readability on any background
                Text(
                    text = app.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = if (isFocused) accentColor else Color.White,
                        fontSize = if (isFocused) 13.5.sp else 12.5.sp,
                        fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.2.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.9f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(0.1f))
            }
        }
    }
}
