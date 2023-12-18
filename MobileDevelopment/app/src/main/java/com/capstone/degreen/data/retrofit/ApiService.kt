package com.capstone.degreen.data.retrofit

import android.media.Image
import com.capstone.degreen.data.model.SoilDetailResponse
import com.capstone.degreen.data.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("soil/{soilType}")
    fun getSoilDetails(
        @Path("soilType") soilType: String,
        @Query("data_requested") dataRequested: String
    ): Call<SoilDetailResponse>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): UploadResponse
}