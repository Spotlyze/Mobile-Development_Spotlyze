package com.bangkit.spotlyze.data.remote.retrofit

import com.bangkit.spotlyze.data.remote.request.FavoriteRequest
import com.bangkit.spotlyze.data.remote.request.LoginRequest
import com.bangkit.spotlyze.data.remote.response.AddFavoriteResponse
import com.bangkit.spotlyze.data.remote.response.ClassifySkinResponse
import com.bangkit.spotlyze.data.remote.response.DeleteFavouriteResponse
import com.bangkit.spotlyze.data.remote.response.GetFavoriteResponseItem
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.remote.response.GetUserProfileResponse
import com.bangkit.spotlyze.data.remote.response.LoginResponse
import com.bangkit.spotlyze.data.remote.response.RegisterResponse
import com.bangkit.spotlyze.data.remote.response.UserUpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @Multipart
    @PUT("profile/{id}")
    suspend fun updateProfilePicture(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part profilePicture: MultipartBody.Part,
    ): UserUpdateResponse

    @Multipart
    @PUT("profile/{id}")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
    ): UserUpdateResponse

    @GET("skincare/{id}")
    suspend fun getSkincareById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): List<GetSkincareResponseItem>

    @GET("skincare")
    suspend fun getAllSkincare(
        @Header("Authorization") token: String
    ): List<GetSkincareResponseItem>

    @GET("profile/{id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): GetUserProfileResponse

    @POST("favorite")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): AddFavoriteResponse

    @HTTP(method = "DELETE", path = "favorite", hasBody = true)
    suspend fun deleteFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): DeleteFavouriteResponse

    @GET("favorite/{id}")
    suspend fun getFavorite(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): List<GetFavoriteResponseItem>

    @Multipart
    @POST("history")
    suspend fun classifySkin(
        @Header("Authorization") token: String,
        @Part("user_id") userId: RequestBody,
        @Part picture: MultipartBody.Part,
        @Part("skin_type") skinType: RequestBody,
        @Part("skin_sensitivity") skinSensitivity: RequestBody,
        @Part("concerns") concerns: RequestBody
    ): ClassifySkinResponse

    @GET("history/user/{id}")
    suspend fun getAllHistory(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): List<GetHistoryResponseItem>
}