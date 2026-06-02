package com.example.dragonhunt.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT locationId FROM unlocked_locations WHERE mapId = :mapId")
    fun observeUnlockedLocationIds(mapId: String): Flow<List<String>>

    @Query("SELECT * FROM unlocked_locations ORDER BY mapName, locationName")
    fun observeAllUnlocked(): Flow<List<UnlockedLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertUnlocked(entity: UnlockedLocationEntity)
}
