package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CampfirePrimary,
    secondary = CampfireSecondary,
    tertiary = CampfireTertiary,
    background = CampfireDarkBackground,
    surface = CampfireDarkSurface,
    onPrimary = Color.White,
    onSecondary = CampfireOnBg,
    onTertiary = Color.White,
    onBackground = CampfireOnBg,
    onSurface = CampfireOnBg,
    surfaceVariant = Color(0xFFF5ECE9),
    onSurfaceVariant = CampfireOnBg
)

private val LightColorScheme = lightColorScheme(
    primary = CampfirePrimary,
    secondary = CampfireSecondary,
    tertiary = CampfireTertiary,
    background = CampfireDarkBackground,
    surface = CampfireDarkSurface,
    onPrimary = Color.White,
    onSecondary = CampfireOnBg,
    onTertiary = Color.White,
    onBackground = CampfireOnBg,
    onSurface = CampfireOnBg,
    surfaceVariant = Color(0xFFF5ECE9),
    onSurfaceVariant = CampfireOnBg
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark Theme for Campfire aesthetic or read system
    content: @Composable () -> Unit,
) {
    // We enforce the beautiful dark premium campfire theme as requested by the designers,
    // but default to DarkColorScheme because it best represents a "campfire" glowing in the night.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
