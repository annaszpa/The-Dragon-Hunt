package com.example.dragonhunt.data

import com.example.dragonhunt.data.local.ProgressDao
import com.example.dragonhunt.data.local.UnlockedLocationEntity
import com.example.dragonhunt.model.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProgressRepository(private val dao: ProgressDao) {

    fun observeAllUnlocked(): Flow<List<UnlockedLocationEntity>> {
        return dao.observeAllUnlocked()
    }

    suspend fun unlockLocation(mapId: String, mapName: String, location: LocationData) {
        withContext(Dispatchers.IO) {
            dao.upsertUnlocked(
                UnlockedLocationEntity(
                    mapId = mapId,
                    mapName = mapName,
                    locationId = location.id,
                    locationName = location.name,
                    description = location.description
                )
            )
        }
    }
}
