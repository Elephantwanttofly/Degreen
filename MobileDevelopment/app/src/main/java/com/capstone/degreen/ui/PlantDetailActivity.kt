package com.capstone.degreen.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.capstone.degreen.databinding.ActivityPlantDetailBinding

class PlantDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPlantDetailBinding.inflate(layoutInflater) }
    private lateinit var tvDetailName : TextView
    private lateinit var tvDescription : TextView
    private lateinit var imgPhoto : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val photo = intent.getStringExtra(EXTRA_PHOTO)

        imgPhoto = binding.ivDetail
        tvDetailName  = binding.tvDetailName
        tvDescription = binding.tvDetailDesc

        tvDetailName.text = name
        tvDescription.text = desc
        Glide.with(this)
            .load(photo)
            .into(imgPhoto)
    }


    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"
    }
}