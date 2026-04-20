package com.example.dragonhunt.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val RoyalSerifFont = FontFamily.Serif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RoyalSerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 1.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RoyalSerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RoyalSerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RoyalSerifFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RoyalSerifFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)
