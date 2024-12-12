package com.bangkit.spotlyze.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.data.source.SortType

class HomeViewModel(private val repository: SkincareRepository) : ViewModel() {

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

    fun getSkincareByType(type: String) = repository.getSkincareByType(type)

}