package com.bangkit.spotlyze.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.spotlyze.data.source.CameraRepository
import com.bangkit.spotlyze.ui.camera.CameraViewModel

class CameraViewModelFactory private constructor(private val cameraRepository: CameraRepository) :
ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(cameraRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: CameraViewModelFactory? = null
        fun getInstance(context: Context) : CameraViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CameraViewModelFactory(
                    CameraRepository.getInstance(context)
                )
            }
    }
}