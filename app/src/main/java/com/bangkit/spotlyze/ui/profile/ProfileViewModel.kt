package com.bangkit.spotlyze.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
        }
    }

    fun getUser() = repository.getSession().asLiveData()

    fun getUserProfile(id: String) = repository.getUserProfile(id)

}