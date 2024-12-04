package com.bangkit.spotlyze.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.spotlyze.data.di.Injection
import com.bangkit.spotlyze.data.repository.UserRepository
import com.bangkit.spotlyze.ui.auth.login.LoginViewModel
import com.bangkit.spotlyze.ui.home.HomeViewModel
import com.bangkit.spotlyze.ui.main.MainViewModel
import com.bangkit.spotlyze.ui.profile.ProfileViewModel
import com.bangkit.spotlyze.ui.skincare.SkincareViewModel

class UserViewModelFactory private constructor(private val userRepo: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepo) as T
        } else if (modelClass.isAssignableFrom(SkincareViewModel::class.java)) {
            return SkincareViewModel(userRepo) as T
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