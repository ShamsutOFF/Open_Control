package com.example.opencontrol.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    fun getKnoById(knoId: Int): Flow<Kno?>
}