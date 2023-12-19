package com.capstone.degreen.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.capstone.degreen.data.model.UploadResponse
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.ActivityResultClassificationBinding
import com.capstone.degreen.ui.camera.CameraActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultClassificationActivity : AppCompatActivity() {
    private val binding by lazy{ ActivityResultClassificationBinding.inflate(layoutInflater)}
    private lateinit var imgPhoto : ImageView
    private var currentImageUri: Uri? = null
    private val TAG: String = "ResultClassificationActivity"
    private val timeStamp: String = SimpleDateFormat(CameraActivity.FILENAME_FORMAT, Locale.US).format(
        Date()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_PHOTO)
        imgPhoto = binding.ivResult
        val imageUri = Uri.parse(imageUriString)
        imgPhoto.setImageURI(imageUri)

        currentImageUri = imageUri

        binding.btnBackHome.setOnClickListener{
            val backToMain = Intent(this@ResultClassificationActivity, MainActivity::class.java)
            startActivity(backToMain)
        }
        getData()
    }

    fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun getData(){
        currentImageUri?.let { uri ->
            binding.tvTypeOfSoil.visibility = View.GONE
            binding.tvPredictionAccuracy.visibility = View.GONE
            binding.btnBackHome.visibility = View.GONE
            val imageFile = uriToFile(uri, this)
            Log.d(TAG, "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            showLoading(true)
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.uploadImage(multipartBody)
                    Log.d(TAG, "sukses : ${successResponse}")
                    showResult(successResponse)
                    showLoading(false)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    errorResponse.success?.let { showToast(it) }
                    Log.d(TAG, "gagal :  ${errorResponse}")
                    showLoading(false)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showResult(response: UploadResponse){
        binding.tvResultSoilName.text = response.prediction?.className
        binding.tvAccuracy.text = response.prediction?.confidenceScore
        binding.tvTypeOfSoil.visibility = View.VISIBLE
        binding.tvPredictionAccuracy.visibility = View.VISIBLE
        binding.btnBackHome.visibility = View.VISIBLE
    }

    companion object{
        const val EXTRA_PHOTO = "extra_photo"
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }
}