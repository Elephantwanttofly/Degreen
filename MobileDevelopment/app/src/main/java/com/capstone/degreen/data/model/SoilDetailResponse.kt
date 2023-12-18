package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class SoilDetailResponse(

	@field:SerializedName("data")
	val data: SoilDetailData? = null,

	@field:SerializedName("status")
	val status: Status? = null
)


data class SoilDetailData(

	@field:SerializedName("deskripsi_tanah")
	val deskripsiTanah: String? = null,

	@field:SerializedName("rekomendasi_bibit")
	val rekomendasiBibit: RekomendasiBibit? = null,

	@field:SerializedName("nama_tanah")
	val namaTanah: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null
)

data class RekomendasiBibit(

	@field:SerializedName("T36K")
	val t36K: T36K? = null,

	@field:SerializedName("T34J")
	val t34J: T34J? = null,

	@field:SerializedName("T39M")
	val t39M: T39M? = null,

	@field:SerializedName("T33D")
	val t33D: T33D? = null,

	@field:SerializedName("T31A")
	val t31A: T31A? = null,

	@field:SerializedName("T35B")
	val t35B: T35B? = null,

	@field:SerializedName("T32P")
	val t32P: T32P? = null,

	@field:SerializedName("T37U")
	val t37U: T37U? = null,

	@field:SerializedName("T40K")
	val t40K: T40K? = null,

	@field:SerializedName("T38B")
	val t38B: T38B? = null
)

data class T37U(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T34J(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T36K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T39M(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T35B(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T33D(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T38B(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T32P(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T40K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T31A(

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