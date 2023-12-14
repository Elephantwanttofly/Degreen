package com.capstone.degreen.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.R
import com.capstone.degreen.data.DataPlant
import com.capstone.degreen.databinding.ActivitySoilDetailBinding
import com.capstone.degreen.ui.adapter.ListPlantAdapter

class SoilDetailActivity : AppCompatActivity() {
    private val binding by lazy{ ActivitySoilDetailBinding.inflate(layoutInflater)}
    private val list = ArrayList<DataPlant>()
    private lateinit var rvplant : RecyclerView
    private lateinit var tvDetailName : TextView
    private lateinit var tvDescription : TextView
    private lateinit var imgPhoto : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val photo = intent.getIntExtra(EXTRA_PHOTO,0)

        imgPhoto = binding.ivSoil
        tvDetailName  = binding.tvSoilName
        tvDescription = binding.tvSoilDesc

        tvDetailName.text = name
        tvDescription.text = desc
        imgPhoto.setImageResource(photo)

        rvplant = binding.rvPlant
        rvplant.setHasFixedSize(true)

        list.addAll(getlistPlants())
        showRecyclerList()
    }

    private fun getlistPlants() : ArrayList<DataPlant>{
        val dataName = resources.getStringArray(R.array.data_name_plant)
        val dataPhoto = resources.getStringArray(R.array.data_photo_plant)
        val dataDescription = resources.getStringArray(R.array.data_description_plant)

        val listPlant = ArrayList<DataPlant>()
        for (i in dataName.indices){
            val plant = DataPlant(dataName[i], dataPhoto[i], dataDescription[i])
            listPlant.add(plant)
        }
        return listPlant
    }

    private fun showRecyclerList() {
        rvplant.layoutManager = GridLayoutManager(this,2)
        val listPlantAdapter = ListPlantAdapter(list)
        rvplant.adapter = listPlantAdapter
    }

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"
    }

}
