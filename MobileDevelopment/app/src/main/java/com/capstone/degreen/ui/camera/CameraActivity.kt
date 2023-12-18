package com.capstone.degreen.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.degreen.data.model.UploadResponse
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.degreen.R
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.ActivityCameraBinding
import com.capstone.degreen.ui.MainActivity
import com.capstone.degreen.ui.ResultClassificationActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater)}
    private var currentImageUri: Uri? = null
    private val TAG: String = "CameraActivity"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    private var imageFile: File? = null
    private val PERMISSION_REQUEST_CODE = 123
    private lateinit var viewModel: CameraViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }

        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION,
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cameraButton.setOnClickListener{startCamera()}
        binding.classificationButton.setOnClickListener{uploadImage()}
    }

    private fun startCamera() {
//        if (currentImageUri == null) {
//            currentImageUri = getImageUri(this)
//            launcherIntentCamera.launch(currentImageUri)
//        }
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else{
            currentImageUri = null
            showToast(getString(R.string.empty_image_warning))
        }
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

    fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.uploadImage(multipartBody)
                    successResponse.success?.let { showToast(it) }
                    Log.d(TAG, "sukses : ${successResponse}")
//                    showLoading(false)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    errorResponse.success?.let { showToast(it) }
                    Log.d(TAG, "gagal :  ${errorResponse}")
//                    showLoading(false)
                }
            }

//            val intent = Intent(this@CameraActivity, ResultClassificationActivity::class.java)
//            intent.putExtra(ResultClassificationActivity.EXTRA_PHOTO, uri.toString())
//            startActivity(intent)
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun startClassification(){
        currentImageUri?.let {
            val intent = Intent(this@CameraActivity, ResultClassificationActivity::class.java)
            intent.putExtra(ResultClassificationActivity.EXTRA_PHOTO, it.toString())
            startActivity(intent)
        }  ?: showToast(getString(R.string.empty_image_warning))

//        val imagePath: String? = getPathFromUri(currentImageUri)
//        val apiService = ApiConfig.getApiService()
//        val imageFile = File(imagePath)
//        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
//        val imagePart = MultipartBody.Part.createFormData( ApiConfig.UPLOAD_IMAGE, imageFile.name, requestFile)
//
//        val call = apiService.uploadImage(imagePart)

//        call.enqueue(object : Callback<UploadResponse>{
//            override fun onResponse(
//                call: Call<UploadResponse>,
//                response: Response<UploadResponse>
//            ) {
//                if(response.isSuccessful){
//                    showData(response.body()!!)
//
//                }
//            }
//
//            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                Log.d(TAG,"errorbro : $t.toString()" )
//            }
//
//        })
    }

    private fun showData(response: UploadResponse){
        Log.d(TAG, "responseUp : $response")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }
}