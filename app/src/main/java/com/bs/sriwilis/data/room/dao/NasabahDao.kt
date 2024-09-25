package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.room.entity.NasabahEntity

@Dao
interface NasabahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nasabahEntity: List<NasabahEntity>)

    @Query("SELECT * FROM nasabah_table")
    suspend fun getAllNasabah(): List<CardNasabah>

    @Query("SELECT * FROM nasabah_table WHERE no_hp_nasabah = :phone")
    suspend fun getNasabahByPhone(phone: String): CardNasabah

    @Query("SELECT * FROM nasabah_table WHERE no_hp_nasabah = :phone")
    suspend fun updateNasabahByPhone(phone: String): NasabahEntity

    @Query("DELETE FROM nasabah_table WHERE no_hp_nasabah = :phone")
    suspend fun deleteUserByPhone(phone: String)

    @Query("SELECT no_hp_nasabah FROM nasabah_table")
    suspend fun getNasabahPhones(): List<String>

}