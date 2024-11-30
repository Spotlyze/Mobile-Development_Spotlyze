package com.bangkit.spotlyze.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.spotlyze.data.repository.UserRepository

class MainViewModel(private val userRepo: UserRepository) : ViewModel() {
    fun getSession() = userRepo.getSession().asLiveData()
}