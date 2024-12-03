package com.bangkit.spotlyze.data.remote.retrofit

import com.bangkit.spotlyze.data.remote.request.LoginRequest
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.remote.response.GetUserProfileResponse
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("skincare")
    suspend fun getAllSkincare(
        @Header("Authorization") token: String
    ) : List<GetSkincareResponseItem>

    @GET("profile/{id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : GetUserProfileResponse
}