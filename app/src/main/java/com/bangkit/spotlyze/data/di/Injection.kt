package com.bangkit.spotlyze.data.di

import android.content.Context
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.local.pref.dataStore
import com.bangkit.spotlyze.data.remote.retrofit.ApiConfig
import com.bangkit.spotlyze.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        return UserRepository.getInstance(pref, apiService)
    }
}