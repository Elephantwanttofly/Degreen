package com.capstone.degreen.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.degreen.R
import com.capstone.degreen.data.model.ListSoilResponse
import com.capstone.degreen.data.model.UploadResponse
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.ActivityMainBinding
import com.capstone.degreen.ui.camera.CameraActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        binding.fabCamera.setOnClickListener { intentCamera() }
    }

    private fun intentCamera(){
        val intent = Intent(this@MainActivity, CameraActivity::class.java)
        startActivity(intent)
    }

    companion object{
        const val TAG = "MainActivity"

    }
}