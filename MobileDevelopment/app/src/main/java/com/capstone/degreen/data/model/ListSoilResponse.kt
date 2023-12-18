package com.capstone.degreen.data.model

import com.google.gson.annotations.SerializedName

data class ListSoilResponse(

	@field:SerializedName("data")
	val data: ListSoilData? = null,

	@field:SerializedName("status")
	val status: ListStatus? = null
)

data class ListSoilData(

	@field:SerializedName("url_tanah")
	val urlTanah: String? = null,

	@field:SerializedName("deskripsi_tanah")
	val deskripsiTanah: String? = null,

	@field:SerializedName("rekomendasi_bibit")
	val rekomendasiBibit: ListRekomendasiBibit? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null
)

data class ListRekomendasiBibit(

	@field:SerializedName("T13J")
	val t13J: T13J? = null,

	@field:SerializedName("T14K")
	val t14K: T14K? = null,

	@field:SerializedName("T17K")
	val t17K: T17K? = null,

	@field:SerializedName("T20C")
	val t20C: T20C? = null,

	@field:SerializedName("T11S")
	val t11S: T11S? = null,

	@field:SerializedName("T15W")
	val t15W: T15W? = null,

	@field:SerializedName("T18J")
	val t18J: T18J? = null,

	@field:SerializedName("T19K")
	val t19K: T19K? = null,

	@field:SerializedName("T12A")
	val t12A: T12A? = null,

	@field:SerializedName("T16B")
	val t16B: T16B? = null
)


data class T12A(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T13J(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T20C(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T15W(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T17K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T11S(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T19K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T18J(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T14K(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class T16B(

	@field:SerializedName("deskripsi_tanaman")
	val deskripsiTanaman: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url_gambar")
	val urlGambar: String? = null,

	@field:SerializedName("url_produk")
	val urlProduk: String? = null
)

data class ListStatus(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
