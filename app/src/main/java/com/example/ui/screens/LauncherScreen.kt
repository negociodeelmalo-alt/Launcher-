package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppCategory
import com.example.ui.components.AppCardItem
import com.example.ui.components.AppContextMenuDialog
import com.example.ui.components.CategoryFilterBar
import com.example.ui.components.ClockWeatherHeader
import com.example.ui.components.LauncherSettingsDialog
import com.example.ui.components.LiquidGlassCard
import com.example.ui.viewmodel.LauncherViewModel

@Composable
fun LauncherScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Show toast messages if any
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // D-Pad focus requester for the first app card
    val firstItemFocusRequester = remember { FocusRequester() }
    var searchExpanded by remember { mutableStateOf(false) }

    // Request initial focus on first app once apps are loaded
    LaunchedEffect(uiState.isLoadingApps, uiState.filteredApps.size) {
        if (!uiState.isLoadingApps && uiState.filteredApps.isNotEmpty()) {
            try {
                firstItemFocusRequester.requestFocus()
            } catch (_: Exception) {
                // Focus request fallback
            }
        }
    }

    // Handle TV Back button: close dialogs or search before exiting launcher
    BackHandler(enabled = uiState.isSettingsOpen || uiState.contextMenuApp != null || searchExpanded) {
        when {
            uiState.isSettingsOpen -> viewModel.closeSettings()
            uiState.contextMenuApp != null -> viewModel.closeContextMenu()
            searchExpanded -> {
                searchExpanded = false
                viewModel.setSearchQuery("")
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Wallpaper Background (User attachment)
        Image(
            painter = painterResource(id = R.drawable.wallpaper_bg),
            contentDescription = "Fondo de Pantalla TV Launcher",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Liquid Glass Backdrop Gradient & Dimming (Allows user customization)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF090D16).copy(alpha = 0.55f),
                            Color(0xFF0B1120).copy(alpha = 0.35f),
                            Color(0xFF070A12).copy(alpha = 0.70f)
                        )
                    )
                )
        )

        // 3. Main Launcher Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp)
        ) {
            // Top Bar: Clock, Date, Weather & Fast Controls
            ClockWeatherHeader(
                currentTime = uiState.currentTime,
                currentDate = uiState.currentDate,
                weatherTemp = uiState.weatherTemp,
                weatherLocation = uiState.weatherLocation,
                config = uiState.config,
                onOpenSettings = { viewModel.openSettings() },
                onOpenSystemSettings = { viewModel.openSystemSettings() }
            )

            // Category Bar & Search Bar Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryFilterBar(
                    selectedCategory = uiState.selectedCategory,
                    accentColor = uiState.config.composeAccentColor,
                    onCategorySelected = { viewModel.setCategory(it) },
                    modifier = Modifier.weight(1f)
                )

                // Search Toggle Button
                LiquidGlassCard(
                    shape = RoundedCornerShape(14.dp),
                    accentColor = uiState.config.composeAccentColor,
                    onClick = { searchExpanded = !searchExpanded },
                    elevation = 2.dp,
                    modifier = Modifier.testTag("search_toggle_button")
                ) { isFocused ->
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = if (isFocused || searchExpanded) uiState.config.composeAccentColor else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (searchExpanded) "Ocultar" else "Buscar",
                            style = TextStyle(
                                color = if (isFocused || searchExpanded) uiState.config.composeAccentColor else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Expandable Search Bar
            AnimatedVisibility(
                visible = searchExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 6.dp)
                ) {
                    LiquidGlassCard(
                        shape = RoundedCornerShape(16.dp),
                        accentColor = uiState.config.composeAccentColor,
                        elevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { _ ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = uiState.config.composeAccentColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                placeholder = {
                                    Text(
                                        text = "Escribe para filtrar aplicaciones...",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 13.sp
                                    )
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = uiState.config.composeAccentColor
                                ),
                                textStyle = TextStyle(fontSize = 14.sp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("search_input_field")
                            )
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.setSearchQuery("") },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Apps Grid (Strict D-Pad remote navigation)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                if (uiState.isLoadingApps) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = uiState.config.composeAccentColor,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                } else if (uiState.filteredApps.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LiquidGlassCard(
                            shape = RoundedCornerShape(18.dp),
                            accentColor = uiState.config.composeAccentColor,
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No se encontraron aplicaciones",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (uiState.selectedCategory == AppCategory.FAVORITES)
                                        "Mantén presionada una aplicación para fijarla como favorita"
                                    else
                                        "Intenta cambiar el término de búsqueda",
                                    style = TextStyle(
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }
                    }
                } else {
                    val gridState = rememberLazyGridState()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(uiState.config.gridColumns),
                        state = gridState,
                        contentPadding = PaddingValues(top = 8.dp, bottom = 28.dp, start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("apps_grid")
                    ) {
                        itemsIndexed(
                            items = uiState.filteredApps,
                            key = { _, item -> item.packageName }
                        ) { index, app ->
                            AppCardItem(
                                app = app,
                                accentColor = uiState.config.composeAccentColor,
                                focusRequester = if (index == 0) firstItemFocusRequester else null,
                                onLaunch = { viewModel.launchApp(app) },
                                onOpenMenu = { viewModel.openContextMenu(app) }
                            )
                        }
                    }
                }
            }
        }

        // 5. Settings Customization Dialog
        if (uiState.isSettingsOpen) {
            LauncherSettingsDialog(
                config = uiState.config,
                currentTime = uiState.currentTime,
                currentDate = uiState.currentDate,
                weatherTemp = uiState.weatherTemp,
                onSetClockColor = { viewModel.setClockColor(it) },
                onSetDateColor = { viewModel.setDateColor(it) },
                onSetWeatherColor = { viewModel.setWeatherColor(it) },
                onSetAccentColor = { viewModel.setAccentColor(it) },
                onSetIs24Hour = { viewModel.setIs24Hour(it) },
                onSetShowSeconds = { viewModel.setShowSeconds(it) },
                onSetGridColumns = { viewModel.setGridColumns(it) },
                onResetDefaults = { viewModel.resetDefaults() },
                onDismiss = { viewModel.closeSettings() }
            )
        }

        // 6. App Context Menu Dialog
        uiState.contextMenuApp?.let { app ->
            AppContextMenuDialog(
                app = app,
                accentColor = uiState.config.composeAccentColor,
                onLaunch = {
                    viewModel.launchApp(app)
                    viewModel.closeContextMenu()
                },
                onToggleFavorite = {
                    viewModel.toggleFavorite(app.packageName)
                    viewModel.closeContextMenu()
                },
                onOpenDetails = {
                    viewModel.openAppDetails(app.packageName)
                },
                onUninstall = {
                    viewModel.requestUninstall(app.packageName)
                },
                onDismiss = { viewModel.closeContextMenu() }
            )
        }
    }
}
