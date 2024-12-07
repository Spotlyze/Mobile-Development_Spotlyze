package com.bangkit.spotlyze.data.di

import android.content.Context
import com.bangkit.spotlyze.data.local.database.room.SkincareDatabase
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.local.pref.dataStore
import com.bangkit.spotlyze.data.remote.retrofit.ApiConfig
import com.bangkit.spotlyze.data.repository.SkinRepository
import com.bangkit.spotlyze.data.repository.SkincareRepository
import com.bangkit.spotlyze.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideSkincareRepository(context: Context): SkincareRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        val dao = SkincareDatabase.getInstance(context).skincareDao()
        return SkincareRepository.getInstance(pref, apiService, dao)
    }

    fun provideSkinRepository(context: Context): SkinRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        return SkinRepository.getInstance(pref, apiService)
    }
}