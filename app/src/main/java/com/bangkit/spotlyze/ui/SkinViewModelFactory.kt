package com.bangkit.spotlyze.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.spotlyze.data.di.Injection
import com.bangkit.spotlyze.data.repository.SkinRepository
import com.bangkit.spotlyze.ui.camera.CameraViewModel
import com.bangkit.spotlyze.ui.history.HistoryViewModel

class SkinViewModelFactory private constructor(private val skinRepo: SkinRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(skinRepo) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(skinRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SkinViewModelFactory? = null
        fun getInstance(context: Context): SkinViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SkinViewModelFactory(Injection.provideSkinRepository(context))
            }
    }
}