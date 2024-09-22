package com.bs.sriwilis.data.mapping

import com.bs.sriwilis.data.response.NasabahResponseDTO
import com.bs.sriwilis.data.room.entity.NasabahEntity

class MappingNasabah {
    fun mapNasabahResponseDtoToEntity(dto: NasabahResponseDTO): List<NasabahEntity> {
        val nasabahEntities = mutableListOf<NasabahEntity>()

        dto.data?.forEach { nasabah ->
            val nasabahEntity = NasabahEntity(
                id = nasabah.id,
                nama_nasabah = nasabah.nama_nasabah,
                no_hp_nasabah = nasabah.no_hp_nasabah,
                alamat_nasabah = nasabah.alamat_nasabah,
                saldo_nasabah = nasabah.saldo_nasabah,
                jasa = nasabah.jasa,
                is_dapat_jasa = nasabah.is_dapat_jasa,
                gambar_nasabah = nasabah.gambar_nasabah ?: ""
            )
            nasabahEntities.add(nasabahEntity)
        }

        return nasabahEntities
    }
}