package com.bangkit.spotlyze.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.bangkit.spotlyze.data.source.Result

class CameraRepository private constructor(private val context: Context) {

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    fun startCamera(lifeCycleOwner: LifecycleOwner, mPreview: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build().also {
                    it.surfaceProvider = mPreview.surfaceProvider
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifeCycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (e: Exception) {
                Log.e("CX Err", e.message.toString())
                Result.Error(e.message.toString())
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun switchCamera(lifeCycleOwner: LifecycleOwner, mPreview: PreviewView) {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        startCamera(lifeCycleOwner, mPreview)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: CameraRepository? = null
        fun getInstance(context: Context): CameraRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CameraRepository(context)
            }.also { INSTANCE = it }
    }
}