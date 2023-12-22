package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class PlantDetailResponse(

	@field:SerializedName("data")
	val data: List<DetailPlant>,

	@field:SerializedName("status")
	val status: Status? = null
)

data class DetailPlant(

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
