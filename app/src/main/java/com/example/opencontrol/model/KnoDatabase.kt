package com.example.opencontrol.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Kno::class], version = 1)
abstract class KnoDatabase : RoomDatabase() {
        abstract fun knoDao(): KnoDao
}