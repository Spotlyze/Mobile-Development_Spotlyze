package com.bangkit.spotlyze.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.spotlyze.data.local.pref.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[USERNAME_KEY] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN] = true
        }
    }

    fun getSession() : Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[ID_KEY] ?: 0,
                preferences[USERNAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun logOut() {
        dataStore.edit {
            it.clear()
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun getCurrentTheme(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }.first()
    }

    suspend fun setThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = intPreferencesKey("id_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val IS_LOGIN = booleanPreferencesKey("is_login")
        private val THEME_KEY = booleanPreferencesKey("theme_key")

        fun getInstance(dataStore: DataStore<Preferences>) =
            INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }

    }
}