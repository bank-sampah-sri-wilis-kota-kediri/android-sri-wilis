package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.data.room.entity.CategoryEntity
import com.bs.sriwilis.data.room.entity.NasabahEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: List<CategoryEntity>)

    @Query("SELECT * FROM category_table")
    suspend fun getAllCategory(): List<CardCategory>

    @Query("SELECT * FROM category_table WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CardCategory

    @Query("SELECT * FROM category_table WHERE id = :id")
    suspend fun updateCategoryById(id: String): NasabahEntity

    @Query("DELETE FROM category_table WHERE id = :id")
    suspend fun deleteCategoryById(id: String)
}