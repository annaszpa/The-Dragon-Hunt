package com.example.dragonhunt.data.local

import androidx.room.Entity

@Entity(
    tableName = "unlocked_locations",
    primaryKeys = ["mapId", "locationId"]
)
data class UnlockedLocationEntity(
    val mapId: String,
    val mapName: String,
    val locationId: String,
    val locationName: String,
    val description: String = "",
    val unlockedAt: Long = System.currentTimeMillis()
)
