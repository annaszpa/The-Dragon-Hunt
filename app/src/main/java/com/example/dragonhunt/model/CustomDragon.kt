package com.example.dragonhunt.model

data class CustomDragon(
    val breed: String,
    val name: String = "",
    val lat: String = "",
    val lng: String = "",
    val description: String = "",
    val isSelected: Boolean = false
)
