package com.example

import androidx.compose.ui.graphics.Color
import com.example.data.preferences.LauncherConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LauncherConfigTest {

    @Test
    fun testDefaultConfig() {
        val config = LauncherConfig()
        assertEquals(0xFFFFFFFF, config.clockColor)
        assertEquals(Color(0xFFFFFFFF), config.composeClockColor)
        assertEquals(Color(0xFF00E5FF), config.composeWeatherColor)
        assertEquals(Color(0xFF00E5FF), config.composeAccentColor)
        assertTrue(config.is24Hour)
        assertEquals(5, config.gridColumns)
    }

    @Test
    fun testCustomColors() {
        val custom = LauncherConfig(
            clockColor = 0xFFFFD600,
            dateColor = 0xFF00E676,
            weatherColor = 0xFFFF6D00,
            accentColor = 0xFFE040FB,
            gridColumns = 6
        )
        assertEquals(Color(0xFFFFD600), custom.composeClockColor)
        assertEquals(Color(0xFF00E676), custom.composeDateColor)
        assertEquals(Color(0xFFFF6D00), custom.composeWeatherColor)
        assertEquals(Color(0xFFE040FB), custom.composeAccentColor)
        assertEquals(6, custom.gridColumns)
    }
}
