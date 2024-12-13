package com.bangkit.spotlyze.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.spotlyze.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()

    fun getCurrentTheme(): Boolean =
        runBlocking {
            repository.getCurrentTheme()
        }

    fun getThemeSetting() = repository.getThemeSetting().asLiveData()
}