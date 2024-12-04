package com.bangkit.spotlyze.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.remote.request.AddFavoriteRequest
import com.bangkit.spotlyze.data.remote.response.AddFavoriteResponse
import com.bangkit.spotlyze.data.remote.response.ErrorResponse
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.remote.retrofit.ApiService
import com.bangkit.spotlyze.data.source.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class SkincareRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService
) {

    private val token = runBlocking { userPref.getSession().first().token }
    val userId = runBlocking { userPref.getSession().first().id }

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

    suspend fun addFavorite(userId: Int, skincareId: Int): Result<AddFavoriteResponse> {
        return try {
            val request = AddFavoriteRequest(userId, skincareId)
            val response = apiService.addFavorite("Bearer $token", request)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage!!)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: SkincareRepository? = null
        fun getInstance(userPref: UserPreference, apiService: ApiService): SkincareRepository =
            instance ?: synchronized(this) {
                instance ?: SkincareRepository(userPref, apiService)
            }
    }
}