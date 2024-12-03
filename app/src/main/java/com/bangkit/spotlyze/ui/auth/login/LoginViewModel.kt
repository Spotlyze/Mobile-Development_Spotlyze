package com.bangkit.spotlyze.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.model.UserModel
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import com.bangkit.spotlyze.data.repository.UserRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepo: UserRepository): ViewModel(){

    private var _loadingStatus = MutableLiveData<Result<LoginResponse>>()
    val loadingStatus: LiveData<Result<LoginResponse>> = _loadingStatus

    fun login(email: String, password: String) {
        _loadingStatus.value = Result.Loading
        viewModelScope.launch {
            _loadingStatus.value = userRepo.login(email, password)
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepo.saveSession(user)
        }
    }
}