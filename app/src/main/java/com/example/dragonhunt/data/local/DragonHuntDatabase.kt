package com.example.dragonhunt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UnlockedLocationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class DragonHuntDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var instance: DragonHuntDatabase? = null

        fun getInstance(context: Context): DragonHuntDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DragonHuntDatabase::class.java,
                    "dragon_hunt.db"
                )
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
            }
        }
    }
}
