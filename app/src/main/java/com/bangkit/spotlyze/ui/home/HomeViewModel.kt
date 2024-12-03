package com.bangkit.spotlyze.ui.home

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.UserRepository

class HomeViewModel(private val repository: UserRepository): ViewModel() {

    fun getAllSkincare() = repository.getAllSkincare()

}