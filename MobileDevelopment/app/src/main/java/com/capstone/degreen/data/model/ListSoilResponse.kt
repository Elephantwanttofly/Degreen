package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class ListSoilResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: Status? = null
)

data class DataItem(

	@field:SerializedName("url_tanah")
	val urlTanah: String? = null,

	@field:SerializedName("deskripsi_tanah")
	val deskripsiTanah: String? = null,

	@field:SerializedName("rekomendasi_bibit")
	val rekomendasiBibit: RekomendasiBibit? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)









