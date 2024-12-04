package com.bangkit.spotlyze.ui.skincare

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.SkincareRepository

class SkincareViewModel(private val repository: SkincareRepository) : ViewModel() {

    fun getSkincareById(id: String) = repository.getSkincareById(id)

}