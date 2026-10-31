package com.example.data.model

import android.graphics.drawable.Drawable

enum class AppCategory(val title: String) {
    ALL("Todas"),
    FAVORITES("Favoritas"),
    MEDIA("Multimedia"),
    TOOLS("Herramientas")
}

data class AppInfo(
    val id: String,
    val name: String,
    val packageName: String,
    val activityName: String?,
    val icon: Drawable?,
    val isFavorite: Boolean = false,
    val isSystemApp: Boolean = false,
    val category: AppCategory = AppCategory.ALL
)
