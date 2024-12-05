package com.bangkit.spotlyze.ui.home

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.SkincareRepository

class HomeViewModel(private val repository: SkincareRepository): ViewModel() {

    fun getAllSkincare() = repository.getAllSkincare()

}