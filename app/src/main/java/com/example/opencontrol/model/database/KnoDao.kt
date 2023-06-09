package com.example.opencontrol.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.opencontrol.model.networkDTOs.Kno
import kotlinx.coroutines.flow.Flow

@Dao
interface KnoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKno(kno: Kno)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKno(knoList: List<Kno>)

    @Query("SELECT * FROM Kno")
    fun getAllKno(): Flow<List<Kno>>

    @Query("SELECT * FROM Kno WHERE id = :knoId")
    suspend fun getKnoById(knoId: Int): Kno?

    @Query("SELECT * FROM Kno WHERE name = :knoName")
    suspend fun getKnoByName(knoName: String): Kno?
}