package com.capstone.degreen.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel()
            }
            else     -> throw java.lang.IllegalArgumentException(
                "Unknown ViewModel class: " + modelClass.name
            )
        } as T
    }
}