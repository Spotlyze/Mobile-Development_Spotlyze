package com.bangkit.spotlyze.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.spotlyze.data.di.Injection
import com.bangkit.spotlyze.data.repository.UserRepository
import com.bangkit.spotlyze.ui.auth.AuthViewModel
import com.bangkit.spotlyze.ui.main.MainViewModel
import com.bangkit.spotlyze.ui.profile.ProfileViewModel

class UserViewModelFactory private constructor(private val userRepo: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserViewModelFactory(Injection.provideUserRepository(context))
            }
    }
}