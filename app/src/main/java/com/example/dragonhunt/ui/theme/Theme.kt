package com.example.dragonhunt.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RoyalColorScheme = darkColorScheme(
    primary = Gold,
    secondary = DarkRed,
    tertiary = AncientGold,
    background = DeepMaroon,
    surface = DarkRed,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Parchment,
    onSurface = Parchment
)

@Composable
fun DragonHuntTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RoyalColorScheme,
        typography = Typography,
        content = content
    )
}
