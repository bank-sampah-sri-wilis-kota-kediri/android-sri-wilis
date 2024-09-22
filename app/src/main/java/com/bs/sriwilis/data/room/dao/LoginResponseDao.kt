package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.room.entity.LoginResponseEntity

@Dao
interface LoginResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loginResponse: LoginResponseEntity)

    @Query("SELECT * FROM login_response_table WHERE id = :id")
    suspend fun getLoginResponseById(id: Int): LoginResponseEntity

    @Query("DELETE FROM login_response_table")
    suspend fun deleteAll()
}