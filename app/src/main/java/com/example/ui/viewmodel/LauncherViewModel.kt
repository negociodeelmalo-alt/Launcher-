package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppCategory
import com.example.data.model.AppInfo
import com.example.data.preferences.LauncherConfig
import com.example.data.preferences.LauncherPreferences
import com.example.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LauncherUiState(
    val apps: List<AppInfo> = emptyList(),
    val filteredApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: AppCategory = AppCategory.ALL,
    val currentTime: String = "--:--",
    val currentDate: String = "Cargando fecha...",
    val weatherTemp: String = "24°C Despejado",
    val weatherLocation: String = "En Vivo",
    val config: LauncherConfig = LauncherConfig(),
    val isSettingsOpen: Boolean = false,
    val contextMenuApp: AppInfo? = null,
    val isLoadingApps: Boolean = true,
    val toastMessage: String? = null
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val preferences = LauncherPreferences(application)

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    private var clockJob: Job? = null

    init {
        // Collect config changes
        viewModelScope.launch {
            preferences.configFlow.collectLatest { config ->
                _uiState.value = _uiState.value.copy(
                    config = config,
                    weatherTemp = config.weatherTemp,
                    weatherLocation = config.weatherCity
                )
                // Re-filter apps when favorites change
                filterApps()
            }
        }

        // Start clock tick
        startClock()

        // Load apps initially
        refreshApps()
    }

    private fun startClock() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (isActive) {
                val now = Date()
                val is24 = _uiState.value.config.is24Hour
                val showSec = _uiState.value.config.showSeconds

                val timePattern = when {
                    is24 && showSec -> "HH:mm:ss"
                    is24 -> "HH:mm"
                    showSec -> "hh:mm:ss a"
                    else -> "hh:mm a"
                }

                val timeFormat = SimpleDateFormat(timePattern, Locale.getDefault())
                val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))

                val timeStr = timeFormat.format(now)
                val rawDate = dateFormat.format(now)
                val dateStr = rawDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                _uiState.value = _uiState.value.copy(
                    currentTime = timeStr,
                    currentDate = dateStr
                )

                delay(1000L)
            }
        }
    }

    fun refreshApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingApps = true)
            val favs = _uiState.value.config.favoritePackages
            val loaded = repository.getInstalledApps(favs)
            _uiState.value = _uiState.value.copy(
                apps = loaded,
                isLoadingApps = false
            )
            filterApps()
        }
    }

    fun setCategory(category: AppCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        filterApps()
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterApps()
    }

    private fun filterApps() {
        val all = _uiState.value.apps
        val query = _uiState.value.searchQuery.trim().lowercase()
        val category = _uiState.value.selectedCategory
        val favs = _uiState.value.config.favoritePackages

        val filtered = all.filter { app ->
            val matchesCategory = when (category) {
                AppCategory.ALL -> true
                AppCategory.FAVORITES -> favs.contains(app.packageName)
                AppCategory.MEDIA -> app.category == AppCategory.MEDIA
                AppCategory.TOOLS -> app.category == AppCategory.TOOLS
            }

            val matchesQuery = if (query.isEmpty()) {
                true
            } else {
                app.name.lowercase().contains(query) || app.packageName.lowercase().contains(query)
            }

            matchesCategory && matchesQuery
        }.map { app ->
            app.copy(isFavorite = favs.contains(app.packageName))
        }

        _uiState.value = _uiState.value.copy(filteredApps = filtered)
    }

    fun launchApp(app: AppInfo) {
        val launched = repository.launchApp(app)
        if (!launched) {
            _uiState.value = _uiState.value.copy(toastMessage = "No se pudo iniciar ${app.name}")
        }
    }

    fun openSystemSettings() {
        repository.openSystemSettings()
    }

    fun openAppDetails(packageName: String) {
        repository.openAppDetails(packageName)
        closeContextMenu()
    }

    fun requestUninstall(packageName: String) {
        repository.requestUninstall(packageName)
        closeContextMenu()
    }

    fun toggleFavorite(packageName: String) {
        preferences.toggleFavorite(packageName)
    }

    fun openSettings() {
        _uiState.value = _uiState.value.copy(isSettingsOpen = true)
    }

    fun closeSettings() {
        _uiState.value = _uiState.value.copy(isSettingsOpen = false)
    }

    fun openContextMenu(app: AppInfo) {
        _uiState.value = _uiState.value.copy(contextMenuApp = app)
    }

    fun closeContextMenu() {
        _uiState.value = _uiState.value.copy(contextMenuApp = null)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }

    // Customization methods
    fun setClockColor(color: Long) {
        preferences.updateClockColor(color)
    }

    fun setDateColor(color: Long) {
        preferences.updateDateColor(color)
    }

    fun setWeatherColor(color: Long) {
        preferences.updateWeatherColor(color)
    }

    fun setAccentColor(color: Long) {
        preferences.updateAccentColor(color)
    }

    fun setIs24Hour(is24: Boolean) {
        preferences.updateIs24Hour(is24)
        startClock()
    }

    fun setShowSeconds(show: Boolean) {
        preferences.updateShowSeconds(show)
        startClock()
    }

    fun setGridColumns(cols: Int) {
        preferences.updateGridColumns(cols)
    }

    fun setGlassDimOpacity(opacity: Float) {
        preferences.updateGlassDimOpacity(opacity)
    }

    fun updateWeatherInfo(city: String, temp: String) {
        preferences.updateWeather(city, temp)
    }

    fun resetDefaults() {
        preferences.resetToDefaults()
    }
}
