package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LauncherConfig(
    val clockColor: Long = 0xFFFFFFFF, // Default Pure White
    val dateColor: Long = 0xFFE0E0E0,  // Default Subtle Silver
    val weatherColor: Long = 0xFF00E5FF, // Default Cyan Glow
    val accentColor: Long = 0xFF00E5FF,  // Default Focus Accent (Neon Cyan)
    val is24Hour: Boolean = true,
    val showSeconds: Boolean = false,
    val gridColumns: Int = 5,
    val glassDimOpacity: Float = 0.35f,
    val favoritePackages: Set<String> = emptySet(),
    val weatherCity: String = "Madrid / En Vivo",
    val weatherTemp: String = "22°C Soleado"
) {
    val composeClockColor: Color get() = Color(clockColor)
    val composeDateColor: Color get() = Color(dateColor)
    val composeWeatherColor: Color get() = Color(weatherColor)
    val composeAccentColor: Color get() = Color(accentColor)
}

class LauncherPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tv_launcher_preferences", Context.MODE_PRIVATE)

    private val _configFlow = MutableStateFlow(loadConfig())
    val configFlow: StateFlow<LauncherConfig> = _configFlow.asStateFlow()

    private fun loadConfig(): LauncherConfig {
        return LauncherConfig(
            clockColor = prefs.getLong(KEY_CLOCK_COLOR, 0xFFFFFFFF),
            dateColor = prefs.getLong(KEY_DATE_COLOR, 0xFFE0E0E0),
            weatherColor = prefs.getLong(KEY_WEATHER_COLOR, 0xFF00E5FF),
            accentColor = prefs.getLong(KEY_ACCENT_COLOR, 0xFF00E5FF),
            is24Hour = prefs.getBoolean(KEY_IS_24_HOUR, true),
            showSeconds = prefs.getBoolean(KEY_SHOW_SECONDS, false),
            gridColumns = prefs.getInt(KEY_GRID_COLUMNS, 5),
            glassDimOpacity = prefs.getFloat(KEY_GLASS_DIM_OPACITY, 0.35f),
            favoritePackages = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet(),
            weatherCity = prefs.getString(KEY_WEATHER_CITY, "Madrid / En Vivo") ?: "Madrid / En Vivo",
            weatherTemp = prefs.getString(KEY_WEATHER_TEMP, "22°C Soleado") ?: "22°C Soleado"
        )
    }

    fun updateClockColor(color: Long) {
        prefs.edit().putLong(KEY_CLOCK_COLOR, color).apply()
        _configFlow.value = _configFlow.value.copy(clockColor = color)
    }

    fun updateDateColor(color: Long) {
        prefs.edit().putLong(KEY_DATE_COLOR, color).apply()
        _configFlow.value = _configFlow.value.copy(dateColor = color)
    }

    fun updateWeatherColor(color: Long) {
        prefs.edit().putLong(KEY_WEATHER_COLOR, color).apply()
        _configFlow.value = _configFlow.value.copy(weatherColor = color)
    }

    fun updateAccentColor(color: Long) {
        prefs.edit().putLong(KEY_ACCENT_COLOR, color).apply()
        _configFlow.value = _configFlow.value.copy(accentColor = color)
    }

    fun updateIs24Hour(is24: Boolean) {
        prefs.edit().putBoolean(KEY_IS_24_HOUR, is24).apply()
        _configFlow.value = _configFlow.value.copy(is24Hour = is24)
    }

    fun updateShowSeconds(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_SECONDS, show).apply()
        _configFlow.value = _configFlow.value.copy(showSeconds = show)
    }

    fun updateGridColumns(cols: Int) {
        prefs.edit().putInt(KEY_GRID_COLUMNS, cols).apply()
        _configFlow.value = _configFlow.value.copy(gridColumns = cols)
    }

    fun updateGlassDimOpacity(opacity: Float) {
        prefs.edit().putFloat(KEY_GLASS_DIM_OPACITY, opacity).apply()
        _configFlow.value = _configFlow.value.copy(glassDimOpacity = opacity)
    }

    fun toggleFavorite(packageName: String) {
        val current = _configFlow.value.favoritePackages.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply()
        _configFlow.value = _configFlow.value.copy(favoritePackages = current)
    }

    fun updateWeather(city: String, temp: String) {
        prefs.edit()
            .putString(KEY_WEATHER_CITY, city)
            .putString(KEY_WEATHER_TEMP, temp)
            .apply()
        _configFlow.value = _configFlow.value.copy(weatherCity = city, weatherTemp = temp)
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
        _configFlow.value = LauncherConfig()
    }

    companion object {
        private const val KEY_CLOCK_COLOR = "clock_color"
        private const val KEY_DATE_COLOR = "date_color"
        private const val KEY_WEATHER_COLOR = "weather_color"
        private const val KEY_ACCENT_COLOR = "accent_color"
        private const val KEY_IS_24_HOUR = "is_24_hour"
        private const val KEY_SHOW_SECONDS = "show_seconds"
        private const val KEY_GRID_COLUMNS = "grid_columns"
        private const val KEY_GLASS_DIM_OPACITY = "glass_dim_opacity"
        private const val KEY_FAVORITES = "favorites"
        private const val KEY_WEATHER_CITY = "weather_city"
        private const val KEY_WEATHER_TEMP = "weather_temp"

        // Preset colors for user customization
        val PRESET_COLORS = listOf(
            0xFFFFFFFF to "Blanco Cristal",
            0xFF00E5FF to "Cian Neón",
            0xFF00E676 to "Verde Menta",
            0xFFFFD600 to "Oro Ámbar",
            0xFFFF6D00 to "Naranja Fuego",
            0xFFE040FB to "Púrpura Eléctrico",
            0xFFFF4081 to "Rosa Rubí",
            0xFF40C4FF to "Azul Celeste",
            0xFF1DE9B6 to "Esmeralda",
            0xFFFF5252 to "Rojo Neón"
        )
    }
}
