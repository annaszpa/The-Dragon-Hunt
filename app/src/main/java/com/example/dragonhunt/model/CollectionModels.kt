package com.example.dragonhunt.model

data class DragonInfo(
    val name: String,
    val description: String,
    val unlocked: Boolean
)

data class RouteCollection(
    val routeName: String,
    val dragons: List<DragonInfo>
)
