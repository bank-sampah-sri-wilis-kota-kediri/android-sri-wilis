package com.bs.sriwilis.data.mapping

import com.bs.sriwilis.data.response.PesananSampahResponseDTO
import com.bs.sriwilis.data.room.entity.PesananSampahEntity
import com.bs.sriwilis.data.room.entity.PesananSampahKeranjangEntity

class MappingSampah {
    fun mapPesananSampahApiResponseDtoToEntities(dto: PesananSampahResponseDTO): Pair<List<PesananSampahKeranjangEntity>, List<PesananSampahEntity>> {
        val keranjangEntities = mutableListOf<PesananSampahKeranjangEntity>()
        val sampahEntities = mutableListOf<PesananSampahEntity>()

        dto.data_keranjang.forEach { keranjang ->
            val keranjangEntity = PesananSampahKeranjangEntity(
                id_pesanan = keranjang.id_pesanan,
                id_nasabah = keranjang.id_nasabah,
                id_petugas = keranjang.id_petugas,
                nominal_transaksi = keranjang.nominal_transaksi,
                tanggal = keranjang.tanggal,
                lat = keranjang.lat,
                lng = keranjang.long,
                status_pesanan = keranjang.status_pesanan,
                created_at = keranjang.created_at,
                updated_at = keranjang.updated_at
            )
            keranjangEntities.add(keranjangEntity)

            keranjang.pesanan_sampah.forEach { sampah ->
                val sampahEntity = PesananSampahEntity(
                    id = keranjang.id,
                    id_pesanan_sampah_keranjang = keranjang.id_pesanan,
                    kategori = sampah.kategori,
                    berat_perkiraan = sampah.berat_perkiraan,
                    harga_perkiraan = sampah.harga_perkiraan,
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