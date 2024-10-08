package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bs.sriwilis.data.repository.modelhelper.CardPenarikan
import com.bs.sriwilis.data.room.entity.PenarikanEntity

@Dao
interface PenarikanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(penarikanEntity: List<PenarikanEntity>)

    @Update
    suspend fun updatePenarikan(penarikan: PenarikanEntity)

    @Delete
    suspend fun deletePenarikan(penarikan: PenarikanEntity)

    @Query("SELECT * FROM penarikan_table WHERE id = :id LIMIT 1")
    suspend fun getPenarikanById(id: Int): PenarikanEntity?

    @Query("SELECT * FROM penarikan_table WHERE id_nasabah = :idNasabah")
    suspend fun getPenarikanByNasabahId(idNasabah: Int): List<PenarikanEntity>

    @Query("SELECT * FROM penarikan_table ORDER BY tanggal DESC")
    suspend fun getAllPenarikan(): List<CardPenarikan>

    // Delete all Penarikan records
    @Query("DELETE FROM penarikan_table")
    suspend fun deleteAllPenarikan()
}