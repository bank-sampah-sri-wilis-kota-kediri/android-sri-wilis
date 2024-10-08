package com.bs.sriwilis.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bs.sriwilis.data.model.Catalog
import com.bs.sriwilis.data.repository.modelhelper.CardCatalog
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.room.entity.CatalogEntity

@Dao
interface CatalogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catalogEntity: List<CatalogEntity>)

    @Query("SELECT * FROM catalog_table")
    suspend fun getAllCatalog(): List<CardCatalog>

    @Query("SELECT * FROM catalog_table WHERE id = :catalogId")
    suspend fun getCatalogById(catalogId: String): CardCatalog

    @Query("DELETE FROM catalog_table")
    suspend fun deleteAll()
}