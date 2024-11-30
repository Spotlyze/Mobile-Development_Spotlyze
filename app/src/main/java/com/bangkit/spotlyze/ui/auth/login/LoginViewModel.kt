package com.bangkit.spotlyze.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.model.UserModel
import com.bangkit.spotlyze.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepo: UserRepository): ViewModel(){

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepo.saveSession(user)
        }
    }
}