package com.bs.sriwilis.data.mapping

import com.bs.sriwilis.data.response.PenarikanResponseDTO
import com.bs.sriwilis.data.room.entity.PenarikanEntity

class MappingPenarikan {
    fun mapPenarikanResponseDtoToEntity(dto: PenarikanResponseDTO): List<PenarikanEntity> {
        val penarikanEntities = mutableListOf<PenarikanEntity>()

        dto.data?.forEach { penarikan ->
            val penarikanEntity = PenarikanEntity(
                id = penarikan.id,
                id_nasabah = penarikan.id_nasabah,
                jenis_penarikan = penarikan.jenis_penarikan,
                nominal = penarikan.nominal,
                tanggal = penarikan.tanggal,
                nomor_meteran = penarikan.nomor_meteran,
                nomor_token = penarikan.nomor_token,
                nomor_rekening = penarikan.nomor_rekening,
                jenis_bank = penarikan.jenis_bank,
                status_penarikan = penarikan.status_penarikan
            )
            penarikanEntities.add(penarikanEntity)
        }

        return penarikanEntities
    }
}
