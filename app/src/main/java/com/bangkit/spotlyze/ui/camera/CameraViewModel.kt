package com.bangkit.spotlyze.ui.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.source.CameraRepository

class CameraViewModel(private val repository: CameraRepository) : ViewModel() {
    fun startCamera(lifeCycleOwner: LifecycleOwner, preview: PreviewView) {
        repository.startCamera(lifeCycleOwner, preview)
    }
    fun switchCamera(lifeCycleOwner: LifecycleOwner, preview: PreviewView) {
        repository.switchCamera(lifeCycleOwner, preview)
    }

}