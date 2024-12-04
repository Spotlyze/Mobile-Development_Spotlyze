package com.bangkit.spotlyze.ui.skincare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.repository.SkincareRepository
import kotlinx.coroutines.launch

class SkincareViewModel(private val repository: SkincareRepository) : ViewModel() {

    fun getSkincareById(id: String) = repository.getSkincareById(id)

    fun setFavSkincare(skincare: SkincareEntity) {
        viewModelScope.launch {
            repository.setFavoriteSkincare(skincare, true)
        }
    }

    fun deleteFavSkincare(skincare: SkincareEntity) {
        viewModelScope.launch {
            repository.setFavoriteSkincare(skincare, false)
        }
    }

    fun getFavoriteSkincare() = repository.getFavoriteSkincare()
}