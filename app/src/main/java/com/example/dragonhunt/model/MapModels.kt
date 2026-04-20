package com.example.dragonhunt.model

import androidx.compose.ui.graphics.Color

data class LocationData(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val unlocked: Boolean = false
)

data class MapData(
    val id: String,
    val name: String,
    val description: String,
    val dragonsCount: Int,
    val centerLat: Double,
    val centerLng: Double
)
