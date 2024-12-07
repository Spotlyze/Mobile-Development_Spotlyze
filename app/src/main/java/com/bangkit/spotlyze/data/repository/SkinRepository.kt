package com.bangkit.spotlyze.data.repository

import android.content.Context
import android.net.Uri
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.remote.response.ClassifySkinResponse
import com.bangkit.spotlyze.data.remote.response.ErrorResponse
import com.bangkit.spotlyze.data.remote.retrofit.ApiService
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.utils.reduceFileImage
import com.bangkit.spotlyze.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class SkinRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService,
) {

    private val token = runBlocking { userPref.getSession().first().token }
    private val userId = runBlocking { userPref.getSession().first().id }.toString()

    suspend fun classifySkin(
        recommendation: String,
        imageUri: Uri,
        context: Context
    ): Result<ClassifySkinResponse> {
        val idReqBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val recommendationReqBody = recommendation.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageFile = uriToFile(imageUri, context).reduceFileImage()
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

        val multiPartImage = MultipartBody.Part.createFormData(
            "history_picture",
            imageFile.name,
            requestImageFile
        )
        return try {
            val response = apiService.classifySkin(
                "Bearer $token",
                userId = idReqBody,
                picture = multiPartImage,
                recommendation = recommendationReqBody
            )
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
        private var instance: SkinRepository? = null
        fun getInstance(userPref: UserPreference, apiService: ApiService): SkinRepository =
            instance ?: synchronized(this) {
                instance ?: SkinRepository(userPref, apiService)
            }
    }
}