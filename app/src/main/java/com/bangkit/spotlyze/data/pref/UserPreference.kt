package com.bangkit.spotlyze.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.spotlyze.data.local.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.name
            preferences[PASS_KEY] = user.password
            preferences[IS_LOGIN] = true
        }
    }

    fun getSession() : Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[PASS_KEY] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun logOut() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private var INSTANCE: UserPreference? = null

        private val PASS_KEY = stringPreferencesKey("pass_key")
        private val EMAIL_KEY = stringPreferencesKey("email_key")
        private val IS_LOGIN = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>) =
            INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }

    }
}