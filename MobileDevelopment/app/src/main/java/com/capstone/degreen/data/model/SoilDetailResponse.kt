package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class SoilDetailResponse(

	@field:SerializedName("data")
	val data: DataSoil,

	@field:SerializedName("status")
	val status: Status? = null
)

data class DataSoil(

	@field:SerializedName("url_tanah")
	val urlTanah: String? = null,

	@field:SerializedName("deskripsi_tanah")
	val deskripsiTanah: String? = null,

	@field:SerializedName("rekomendasi_bibit")
	val rekomendasiBibit: List<RekomendasiBibitItem>,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class RekomendasiBibitItem(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("id_tanaman")
	val idTanaman: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
