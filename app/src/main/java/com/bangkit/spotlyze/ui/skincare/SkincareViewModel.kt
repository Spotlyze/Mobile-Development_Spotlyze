package com.bangkit.spotlyze.ui.skincare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.remote.response.AddFavoriteResponse
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class SkincareViewModel(private val repository: SkincareRepository) : ViewModel() {

    private var _addFavorite: MutableLiveData<Result<AddFavoriteResponse>> = MutableLiveData()
    val addFavorite: LiveData<Result<AddFavoriteResponse>> = _addFavorite

    val userId = repository.userId

    fun getSkincareById(id: String) = repository.getSkincareById(id)

    fun addFavorite(userId: Int, skincareId: Int) {
        _addFavorite.value = Result.Loading
        viewModelScope.launch {
            _addFavorite.value = repository.addFavorite(userId, skincareId)
        }
    }


}