package com.bs.sriwilis.data.mapping

import com.bs.sriwilis.data.response.KeranjangTransaksiResponseDTO
import com.bs.sriwilispetugas.data.room.KeranjangTransaksiEntity
import com.bs.sriwilispetugas.data.room.TransaksiSampahEntity

class MappingTransaksi {
    fun mapTransaksiSampahApiResponseDtoToEntities(dto: KeranjangTransaksiResponseDTO): Pair<List<KeranjangTransaksiEntity>, List<TransaksiSampahEntity>> {
        val keranjangEntities = mutableListOf<KeranjangTransaksiEntity>()
        val sampahEntities = mutableListOf<TransaksiSampahEntity>()

        dto.data.forEach { keranjang ->
            val keranjangEntity = KeranjangTransaksiEntity(
                id = keranjang.id,
                id_nasabah = keranjang.id_nasabah,
                nominal_transaksi = keranjang.nominal_transaksi,
                tanggal = keranjang.tanggal,
                status_transaksi = keranjang.status_transaksi,
                created_at = keranjang.created_at,
                updated_at = keranjang.updated_at
            )
            keranjangEntities.add(keranjangEntity)

            keranjang.transaksi_sampah?.forEach { sampah ->
                val sampahEntity = TransaksiSampahEntity(
                    id_keranjang_transaksi = sampah.id_keranjang_transaksi,
                    kategori = sampah.kategori,
                    berat = sampah.berat,
                    harga = sampah.harga,
                    gambar = sampah.gambar,
                    created_at = sampah.created_at,
                    updated_at = sampah.updated_at
                )
                sampahEntities.add(sampahEntity)
            }
        }

        return Pair(keranjangEntities, sampahEntities)
    }
}