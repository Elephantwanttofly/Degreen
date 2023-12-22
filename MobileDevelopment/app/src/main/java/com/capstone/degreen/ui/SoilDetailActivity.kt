package com.capstone.degreen.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.degreen.data.model.Constant
import com.capstone.degreen.data.model.RekomendasiBibitItem
import com.capstone.degreen.data.model.SoilDetailResponse
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.ActivitySoilDetailBinding
import com.capstone.degreen.ui.adapter.ListPlantAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoilDetailActivity : AppCompatActivity() {
    private val binding by lazy{ ActivitySoilDetailBinding.inflate(layoutInflater)}
    private lateinit var rvplant : RecyclerView
    private lateinit var listplantAdapter: ListPlantAdapter
    private val TAG: String = "SoilDetailActivity"
    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvplant = binding.rvPlant
        rvplant.setHasFixedSize(true)

        getSoil()
        showRecyclerList()
    }


    private fun getSoil(){
        showLoading(true)
        ApiConfig.getApiService().getSoilDetails(Constant.SOIL_ID)
            .enqueue(object : Callback<SoilDetailResponse> {
                override fun onResponse(
                    call: Call<SoilDetailResponse>,
                    response: Response<SoilDetailResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful){
                        val soil = response.body()
                        showData(response.body()!!)
                        Log.d(TAG,"ResponJadi : ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<SoilDetailResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d(TAG,"GagalAmbilDetail {t}" )
                }


            })
    }


    private fun showData(response: SoilDetailResponse){
        binding.tvSoilName.text = response.data.jenis
        binding.tvSoilDesc.text = response.data.deskripsiTanah
        id = response.data.id
        val UrlSoil = Constant.BASE_URL + response.data.urlTanah
        Glide.with(this)
            .load(UrlSoil)
            .into(binding.ivSoil)
        listplantAdapter.setData(response.data.rekomendasiBibit)
//        Log.d(TAG, "IsinyaAPAA: $rekomendasiBibitUrls")

    }


//    private fun showPlant(response: RecommendationPlantResponse){
//        listplantAdapter.setData(response.data)
//    }

    private fun showRecyclerList() {
        listplantAdapter = ListPlantAdapter(arrayListOf(), object : ListPlantAdapter.OnAdapterListenerPlant{
            override fun onClick(plant: RekomendasiBibitItem) {
                Constant.PLANT_ID = plant.urlGambar!!
                Log.d(TAG, "Plantid : ${plant.urlGambar}")

                val intent = Intent(this@SoilDetailActivity, PlantDetailActivity::class.java)
                intent.putExtra(PlantDetailActivity.EXTRAID, id)
                startActivity(intent)
            }

        })
        rvplant.apply{
            layoutManager = GridLayoutManager(context,2)
            adapter = listplantAdapter
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



}
