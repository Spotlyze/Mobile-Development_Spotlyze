package com.bangkit.spotlyze.ui.skincare

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.UserRepository

class SkincareViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSkincareById(id: String) = repository.getSkincareById(id)

}