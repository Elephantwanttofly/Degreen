package com.capstone.degreen.data.retrofit


import com.capstone.degreen.data.model.ListSoilResponse
import com.capstone.degreen.data.model.PlantDetailResponse
import com.capstone.degreen.data.model.SoilDetailResponse
import com.capstone.degreen.data.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("soil")
    fun getSoil(
    ): Call<ListSoilResponse>

    @GET("soil/{id}")
    fun getSoilDetails(
        @Path("id") id: String
    ): Call<SoilDetailResponse>

    @GET("soil/{id}/rekomendasi_bibit")
    fun getPlantDetails(
        @Path("id") id: String
    ): Call<PlantDetailResponse>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): UploadResponse
}