package com.bangkit.spotlyze.ui.skincare

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.data.source.SortType
import kotlinx.coroutines.launch

class SkincareViewModel(private val repository: SkincareRepository) : ViewModel() {

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _sort = MutableLiveData<SortType>()

    init {
        _sort.value = SortType.RANDOM
    }

    fun changeSortType(sortType: SortType) {
        _sort.value = sortType
    }

    fun getAllSkincare() = _sort.switchMap {
        repository.getAllSkincare(it)
    }

    fun getSkincareById(id: String) = repository.getSkincareById(id)

    fun addFavorite(skincareId: Int) {
        viewModelScope.launch {
            repository.addFavorite(skincareId)
            _isFavorite.value = true
            Log.d("okhttp", "add favorite: ${_isFavorite.value}")
        }
    }

    fun deleteFavorite(skincareId: Int) {
        viewModelScope.launch {
            repository.deleteFavorite(skincareId)
            _isFavorite.value = false
            Log.d("okhttp", "delete favorite: ${_isFavorite.value}")
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

    fun isFavoriteSkincare(skincareId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavoriteSkincare(skincareId)
        }
    }
}