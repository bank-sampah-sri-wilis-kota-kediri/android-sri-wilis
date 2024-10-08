package com.bs.sriwilis.data.mapping

import com.bs.sriwilis.data.response.CatalogResponseDTO
import com.bs.sriwilis.data.room.entity.CatalogEntity

class MappingKatalog {
    fun mapCatalogResponseDtoToEntity(dto: CatalogResponseDTO): List<CatalogEntity> {
        val catalogEntities = mutableListOf<CatalogEntity>()

        dto.data?.forEach { katalog ->
            val catalogEntity = CatalogEntity(
                id = katalog.id,
                judul_katalog = katalog.judul_katalog,
                deskripsi_katalog = katalog.deskripsi_katalog,
                harga_katalog = katalog.harga_katalog,
                no_wa = katalog.no_wa,
                shopee_link = katalog.shopee_link,
                gambar_katalog = katalog.gambar_katalog

            )
            catalogEntities.add(catalogEntity)
        }

        return catalogEntities
    }
}