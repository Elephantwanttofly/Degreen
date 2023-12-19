package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class SoilDetailResponse(

	@field:SerializedName("data")
	val data: List<SoilDetail?>? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class SoilDetail(

	@field:SerializedName("url_tanah")
	val urlTanah: String? = null,

	@field:SerializedName("deskripsi_tanah")
	val deskripsiTanah: String? = null,

	@field:SerializedName("rekomendasi_bibit")
	val rekomendasiBibit: RekomendasiBibit? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null
)

data class T10B(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class T01P(

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

data class T04J(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T08T(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T03M(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T09T(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class RekomendasiBibit(

	@field:SerializedName("T03M")
	val t03M: T03M? = null,

	@field:SerializedName("T09T")
	val t09T: T09T? = null,

	@field:SerializedName("T04J")
	val t04J: T04J? = null,

	@field:SerializedName("T07K")
	val t07K: T07K? = null,

	@field:SerializedName("T02D")
	val t02D: T02D? = null,

	@field:SerializedName("T10B")
	val t10B: T10B? = null,

	@field:SerializedName("T05S")
	val t05S: T05S? = null,

	@field:SerializedName("T01P")
	val t01P: T01P? = null,

	@field:SerializedName("T06B")
	val t06B: T06B? = null,

	@field:SerializedName("T08T")
	val t08T: T08T? = null
)

data class T07K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T02D(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T05S(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T06B(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)
