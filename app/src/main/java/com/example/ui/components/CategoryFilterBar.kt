package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCategory

@Composable
fun CategoryFilterBar(
    selectedCategory: AppCategory,
    accentColor: Color,
    onCategorySelected: (AppCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val categories = listOf(
            Triple(AppCategory.ALL, "Todas", Icons.Default.Apps),
            Triple(AppCategory.FAVORITES, "Favoritas", Icons.Default.Star),
            Triple(AppCategory.MEDIA, "Multimedia", Icons.Default.Movie),
            Triple(AppCategory.TOOLS, "Herramientas", Icons.Default.Build)
        )

        categories.forEach { (cat, title, icon) ->
            val isSelected = selectedCategory == cat
            CategoryChip(
                title = title,
                icon = icon,
                isSelected = isSelected,
                accentColor = accentColor,
                onClick = { onCategorySelected(cat) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    LiquidGlassCard(
        shape = RoundedCornerShape(14.dp),
        accentColor = accentColor,
        onClick = onClick,
        elevation = if (isSelected) 6.dp else 2.dp
    ) { isFocused ->
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = when {
                    isFocused -> accentColor
                    isSelected -> accentColor
                    else -> Color.White.copy(alpha = 0.75f)
                },
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = TextStyle(
                    color = when {
                        isFocused -> accentColor
                        isSelected -> Color.White
                        else -> Color.White.copy(alpha = 0.8f)
                    },
                    fontSize = 13.sp,
                    fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}
