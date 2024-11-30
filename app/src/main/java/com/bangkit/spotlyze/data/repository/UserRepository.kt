package com.bangkit.spotlyze.data.repository

import com.bangkit.spotlyze.data.local.model.UserModel
import com.bangkit.spotlyze.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val userPref: UserPreference){

    suspend fun saveSession(user: UserModel) {
        userPref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }

    suspend fun logOut() {
        userPref.logOut()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPref: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPref)
            }
    }
}