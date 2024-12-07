package com.bangkit.spotlyze.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.local.pref.model.UserModel
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import com.bangkit.spotlyze.data.remote.response.RegisterResponse
import com.bangkit.spotlyze.data.repository.UserRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository): ViewModel(){

    private var _loginStatus = MutableLiveData<Result<LoginResponse>>()
    val loginStatus: LiveData<Result<LoginResponse>> = _loginStatus

    private var _registerStatus = MutableLiveData<Result<RegisterResponse>>()
    val registerStatus: LiveData<Result<RegisterResponse>> = _registerStatus

    fun register(email: String, name: String, password: String) {
        _registerStatus.value = Result.Loading
        viewModelScope.launch {
            _registerStatus.value = repository.register(email, name, password)
        }
    }

    fun login(email: String, password: String) {
        _loginStatus.value = Result.Loading
        viewModelScope.launch {
            _loginStatus.value = repository.login(email, password)
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}