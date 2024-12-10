package com.bangkit.spotlyze.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.remote.response.UserUpdateResponse
import com.bangkit.spotlyze.data.repository.UserRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private var _updatePictureState: MutableLiveData<Result<UserUpdateResponse>> = MutableLiveData()
    val updatePictureState: MutableLiveData<Result<UserUpdateResponse>> = _updatePictureState

    private var _updateInfoState: MutableLiveData<Result<UserUpdateResponse>> = MutableLiveData()
    val updateInfoState: MutableLiveData<Result<UserUpdateResponse>> = _updateInfoState


    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
        }
    }

    fun getUser() = repository.getSession().asLiveData()

    fun getUserProfile(id: String) = repository.getUserProfile(id)

    fun updateProfilePicture(userProfile: Uri, context: Context) {
        _updatePictureState.value = Result.Loading
        viewModelScope.launch {
            _updatePictureState.value = repository.updateProfilePicture(userProfile, context)
        }
    }

    fun updateUserInfo(name: String, email: String, context: Context) {
        _updateInfoState.value = Result.Loading
        viewModelScope.launch {
            _updateInfoState.value = repository.updateUserInfo(name, email, context)
        }

    }
}