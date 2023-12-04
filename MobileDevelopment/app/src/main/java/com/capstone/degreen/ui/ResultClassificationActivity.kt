package com.capstone.degreen.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.R
import com.capstone.degreen.data.DataPlant
import com.capstone.degreen.databinding.ActivityResultClassificationBinding
import com.capstone.degreen.ui.adapter.ListPlantAdapter

class ResultClassificationActivity : AppCompatActivity() {
    private val binding by lazy{ ActivityResultClassificationBinding.inflate(layoutInflater)}
    private val list = ArrayList<DataPlant>()
    private lateinit var rvplant : RecyclerView
    private lateinit var imgPhoto : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_PHOTO)
        imgPhoto = binding.ivResult
        val imageUri = Uri.parse(imageUriString)
        imgPhoto.setImageURI(imageUri)

        rvplant = binding.rvPlant
        rvplant.setHasFixedSize(true)

        list.addAll(getlistPlants())
        showRecyclerList()
    }

    private fun getlistPlants() : ArrayList<DataPlant>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataPhoto = resources.getStringArray(R.array.data_photo)
        val dataDescription = resources.getStringArray(R.array.data_description)

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
        const val EXTRA_PHOTO = "extra_photo"
    }
}
