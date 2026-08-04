package com.pakexciseinfo.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Sea,
    onPrimary = Color.White,
    primaryContainer = Sand,
    onPrimaryContainer = SeaDeep,
    secondary = Gold,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFFFF1CC),
    onSecondaryContainer = Ink,
    tertiary = InkSoft,
    background = Fog,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = SurfaceSoft,
    onSurfaceVariant = Muted,
    outline = Line,
)

private val DarkColors = darkColorScheme(
    primary = SeaDark,
    onPrimary = FogDark,
    primaryContainer = Color(0xFF1A3D28),
    onPrimaryContainer = SeaDeepDark,
    secondary = Gold,
    onSecondary = FogDark,
    secondaryContainer = Color(0xFF3A2E12),
    onSecondaryContainer = Color(0xFFFFF1CC),
    tertiary = InkSoftDark,
    background = FogDark,
    onBackground = InkDark,
    surface = PaperDark,
    onSurface = InkDark,
    surfaceVariant = SurfaceSoftDark,
    onSurfaceVariant = MutedDark,
    outline = Color(0x293ECF7A),
)

@Composable
fun PakExciseTheme(
    // Match the website’s default light look instead of following system dark mode.
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
