package com.bangkit.spotlyze.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.local.pref.model.UserModel
import com.bangkit.spotlyze.data.remote.request.LoginRequest
import com.bangkit.spotlyze.data.remote.response.ErrorResponse
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.remote.response.GetUserProfileResponse
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import com.bangkit.spotlyze.data.remote.retrofit.ApiService
import com.bangkit.spotlyze.data.source.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService
) {

    private val token = runBlocking { userPref.getSession().first().token }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            Log.d("okhttp", "login response: $response")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.e("okhttp", "login error: $jsonInString")
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("okhttp", "login error: $errorMessage")
            Result.Error(errorMessage!!)
        } catch (e: Exception) {
            Log.e("okhttp", "login error: ${e.message}")
            Result.Error(e.message.toString())
        }
    }

    fun getUserProfile(id: String): LiveData<Result<GetUserProfileResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getUserProfile("Bearer $token", id)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getAllSkincare(): LiveData<Result<List<GetSkincareResponseItem>>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getAllSkincare("Bearer $token")
                Log.d("okhttp", "getAllSkincare response: $response")
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getSkincareById(id: String): LiveData<Result<List<GetSkincareResponseItem>>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getSkincareById("Bearer $token", id)
                Log.d("okhttp", "getSkincareById response: $response")
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    //preferences
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
        fun getInstance(userPref: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPref, apiService)
            }
    }
}