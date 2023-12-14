package com.capstone.degreen.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.capstone.degreen.databinding.ActivityResultClassificationBinding

class ResultClassificationActivity : AppCompatActivity() {
    private val binding by lazy{ ActivityResultClassificationBinding.inflate(layoutInflater)}
    private lateinit var imgPhoto : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_PHOTO)
        imgPhoto = binding.ivResult
        val imageUri = Uri.parse(imageUriString)
        imgPhoto.setImageURI(imageUri)

        binding.btnBackHome.setOnClickListener{
            val backToMain = Intent(this@ResultClassificationActivity, MainActivity::class.java)
            startActivity(backToMain)
        }
    }

    companion object{
        const val EXTRA_PHOTO = "extra_photo"
    }
}