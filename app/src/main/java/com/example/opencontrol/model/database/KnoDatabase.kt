package com.example.opencontrol.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.opencontrol.model.database.KnoDao
import com.example.opencontrol.model.networkDTOs.Kno

@Database(entities = [Kno::class], version = 1)
abstract class KnoDatabase : RoomDatabase() {
    abstract fun knoDao(): KnoDao
}