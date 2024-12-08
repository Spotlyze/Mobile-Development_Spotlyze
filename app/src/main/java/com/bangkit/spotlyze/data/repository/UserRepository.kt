package com.bangkit.spotlyze.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.local.pref.model.UserModel
import com.bangkit.spotlyze.data.remote.request.LoginRequest
import com.bangkit.spotlyze.data.remote.response.ErrorResponse
import com.bangkit.spotlyze.data.remote.response.GetUserProfileResponse
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import com.bangkit.spotlyze.data.remote.response.RegisterResponse
import com.bangkit.spotlyze.data.remote.response.UserUpdateResponse
import com.bangkit.spotlyze.data.remote.retrofit.ApiService
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.utils.reduceFileImage
import com.bangkit.spotlyze.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService
) {

    private val token = runBlocking { userPref.getSession().first().token }
    private val userId = runBlocking { userPref.getSession().first().id }

    suspend fun register(email: String, name: String, password: String): Result<RegisterResponse> {
        val emailReqBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameReqBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordReqBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        return try {
            val response = apiService.register(nameReqBody, emailReqBody, passwordReqBody)
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

    suspend fun updateProfilePicture(
        profilePicture: Uri,
        context: Context
    ): Result<UserUpdateResponse> {

        val imageFile = uriToFile(profilePicture, context).reduceFileImage()
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multiPartImage = MultipartBody.Part.createFormData(
            "profile_picture",
            imageFile.name,
            requestImageFile
        )
        return try {
            val response = apiService.updateProfilePicture(
                "Bearer $token",
                userId.toString(),
                multiPartImage,
            )
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage.toString())
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun updateUserInfo(
        name: String,
        email: String,
        context: Context
    ): Result<UserUpdateResponse> {

        val nameReqBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailReqBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        return try {
            val response = apiService.updateUserInfo(
                "Bearer $token",
                userId.toString(),
                nameReqBody,
                emailReqBody
            )
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage.toString())
        } catch (e: Exception) {
            Result.Error(e.message.toString())
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