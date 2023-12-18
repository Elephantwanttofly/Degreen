package com.capstone.degreen.ui.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel : ViewModel(){
    private val uploadInput = MutableLiveData<Pair<String, File>>()
    fun uploadImage(description: String, file: File): Job = viewModelScope.launch {
        uploadInput.value = Pair(description, file)
    }
}