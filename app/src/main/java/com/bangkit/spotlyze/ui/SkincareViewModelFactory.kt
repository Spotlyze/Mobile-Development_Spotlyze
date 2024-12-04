package com.bangkit.spotlyze.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.spotlyze.data.di.Injection
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.ui.home.HomeViewModel
import com.bangkit.spotlyze.ui.skincare.SkincareViewModel

class SkincareViewModelFactory private constructor(private val skincareRepo: SkincareRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkincareViewModel::class.java)) {
            return SkincareViewModel(skincareRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(skincareRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: SkincareViewModelFactory? = null
        fun getInstance(context: Context): SkincareViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SkincareViewModelFactory(Injection.provideSkincareRepository(context))
            }
    }
}