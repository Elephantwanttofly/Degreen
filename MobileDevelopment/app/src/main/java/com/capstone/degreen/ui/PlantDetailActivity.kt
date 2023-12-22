package com.capstone.degreen.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.capstone.degreen.data.model.Constant
import com.capstone.degreen.data.model.DetailPlant
import com.capstone.degreen.data.model.PlantDetailResponse
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.ActivityPlantDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPlantDetailBinding.inflate(layoutInflater) }
    private lateinit var tvDetailName : TextView
    private lateinit var tvDescription : TextView
    private lateinit var imgPhoto : ImageView
    private val TAG: String = "PlantDetailActivity"
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.getStringExtra(EXTRAID).toString()

        getPlant()
    }

    private fun getPlant(){
        showLoading(true)
        ApiConfig.getApiService().getPlantDetails(id)
            .enqueue(object : Callback<PlantDetailResponse> {
                override fun onResponse(
                    call: Call<PlantDetailResponse>,
                    response: Response<PlantDetailResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful){
                        showData(response.body()!!)
                        Log.d(TAG,"ResponPlant : ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<PlantDetailResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d(TAG,"GagalAmbilDetail {t}" )
                }


            })
    }

    private fun showData(response: PlantDetailResponse){
//        val firstDetailPlant: DetailPlant? = response.data.firstOrNull()
//        if (firstDetailPlant != null) {
//            binding.tvDetailName.text = firstDetailPlant.nama
//            binding.tvDetailDesc.text = firstDetailPlant.deskripsiTanaman
//            val UrlPlant = Constant.BASE_URL + firstDetailPlant.urlGambar
//            Glide.with(this)
//                .load(UrlPlant)
//                .into(binding.ivDetail)
//            binding.btnLink.setOnClickListener{
//                openEcommerceApp(firstDetailPlant.urlProduk)
//            }
//        }

        val firstDetailPlant: DetailPlant? = response.data.firstOrNull()
        // Memeriksa apakah ada SoilDetail yang ditemukan
        if (firstDetailPlant != null) {
            val urlGambar: String? = firstDetailPlant.urlGambar
            val detail : String? = firstDetailPlant.deskripsiTanaman
            val UrlPlant = Constant.BASE_URL + firstDetailPlant?.urlGambar

            if (urlGambar != null && detail!= null && UrlPlant != null) {
                binding.tvDetailName.text = firstDetailPlant?.nama
                binding.tvDetailDesc.text = firstDetailPlant?.deskripsiTanaman
                Glide.with(this)
                    .load(UrlPlant)
                    .into(binding.ivDetail)
                binding.btnLink.setOnClickListener{
                    openEcommerceApp(firstDetailPlant?.urlProduk)
                }
            } else {
                Log.d(TAG, "error")
            }
        }
    }

    private fun openEcommerceApp(uri: String?){
        // Contoh deep link
        val deepLinkUri = Uri.parse(uri)  // Ganti dengan URI deep link aplikasi Anda

        // Buat Intent dengan ACTION_VIEW untuk membuka deep link
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

        // Cek apakah ada aplikasi yang dapat menangani deep link
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle ketika tidak ada aplikasi yang dapat menangani deep link
            // Misalnya, tampilkan pesan bahwa deep link tidak dapat dibuka
        }
    }

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> {
                binding.pbSoilDetail.visibility = View.VISIBLE
            }
            false -> {
                binding.pbSoilDetail.visibility = View.GONE
            }
        }
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRAID = "Extraid"
    }
}