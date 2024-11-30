package com.bangkit.spotlyze.data.di

import android.content.Context
import com.bangkit.spotlyze.data.pref.UserPreference
import com.bangkit.spotlyze.data.pref.dataStore
import com.bangkit.spotlyze.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}