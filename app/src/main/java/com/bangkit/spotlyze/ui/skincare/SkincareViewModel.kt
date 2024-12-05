package com.bangkit.spotlyze.ui.skincare

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.remote.response.AddFavoriteResponse
import com.bangkit.spotlyze.data.remote.response.DeleteFavouriteResponse
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class SkincareViewModel(private val repository: SkincareRepository) : ViewModel() {

    private var _addFavoriteState = MutableLiveData<Result<AddFavoriteResponse>>()
    val addFavoriteState: LiveData<Result<AddFavoriteResponse>> = _addFavoriteState

    private var _deleteFavoriteState = MutableLiveData<Result<DeleteFavouriteResponse>>()
    val deleteFavoriteState: LiveData<Result<DeleteFavouriteResponse>> = _deleteFavoriteState

    fun getSkincareById(id: String) = repository.getSkincareById(id)

    fun addFavorite(skincareId: Int) {
        viewModelScope.launch {
            _addFavoriteState.value = repository.addFavorite(skincareId)
            Log.d("okhttp", "add favorite: ${_addFavoriteState.value}")
        }
    }

    fun deleteFavorite(skincareId: Int) {
        viewModelScope.launch {
            _deleteFavoriteState.value = repository.deleteFavorite(skincareId)
            Log.d("okhttp", "delete favorite: ${_deleteFavoriteState.value}")
        }
    }

    fun setFavSkincareDao(skincare: SkincareEntity) {
        viewModelScope.launch {
            repository.setFavoriteSkincare(skincare, true)
        }
    }

    fun deleteFavSkincareDao(skincare: SkincareEntity) {
        viewModelScope.launch {
            repository.setFavoriteSkincare(skincare, false)
        }
    }

    fun getFavorite() = repository.getFavorite()
}