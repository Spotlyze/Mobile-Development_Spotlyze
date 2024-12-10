package com.bangkit.spotlyze.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.local.database.room.SkincareDao
import com.bangkit.spotlyze.data.local.pref.UserPreference
import com.bangkit.spotlyze.data.remote.request.FavoriteRequest
import com.bangkit.spotlyze.data.remote.response.AddFavoriteResponse
import com.bangkit.spotlyze.data.remote.response.DeleteFavouriteResponse
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
    private val apiService: ApiService,
    private val dao: SkincareDao
) {

    private val token = runBlocking { userPref.getSession().first().token }
    private val userId = runBlocking { userPref.getSession().first().id }

    fun getAllSkincare(): LiveData<Result<List<SkincareEntity>>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response =
                    apiService.getAllSkincare("Bearer $token")
                val skincareList = response.map { skincare ->
                    val isFavorite = dao.isSkincareFavorite(skincare.skincareId!!)
                    SkincareEntity(
                        skincareId = skincare.skincareId,
                        name = skincare.name,
                        brand = skincare.brand,
                        category = skincare.category,
                        rating = skincare.starRating,
                        isRecommend = skincare.isRecommend == 1,
                        description = skincare.descriptionProcessed,
                        skinType = skincare.skinType,
                        price = skincare.price,
                        skincarePicture = skincare.skincarePicture,
                        ingredients = skincare.ingredients,
                        isFavorite = isFavorite
                    )
                }
                dao.deleteAll()
                dao.insertSkincare(skincareList)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
            val localData: LiveData<Result<List<SkincareEntity>>> =
                dao.getAllSkincare().map { Result.Success(it) }
            emitSource(localData)
        }
    }

    fun getFavoriteSkincare(): LiveData<List<SkincareEntity>> {
        return dao.getFavoriteSkincare()
    }

    suspend fun setFavoriteSkincare(skincare: SkincareEntity, favoriteState: Boolean) {
        skincare.isFavorite = favoriteState
        dao.updateSkincare(skincare)
    }

    fun getSkincareById(id: String): LiveData<Result<List<SkincareEntity>>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getSkincareById("Bearer $token", id)
                val isFavorite = dao.isSkincareFavorite(response[0].skincareId!!)
                SkincareEntity(
                    skincareId = response[0].skincareId,
                    name = response[0].name,
                    skinType = response[0].skinType,
                    price = response[0].price,
                    skincarePicture = response[0].skincarePicture,
                    ingredients = response[0].ingredients,
                    explanation = response[0].descriptionProcessed,
                    rating = response[0].starRating,

                    isFavorite = isFavorite,
                )
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
            val localData: LiveData<Result<List<SkincareEntity>>> =
                dao.getSkincareById(id.toInt()).map { Result.Success(it) }
            emitSource(localData)
        }
    }


    suspend fun isFavoriteSkincare(skincareId: Int): Boolean {
        return try {
            val listSkincare = apiService.getFavorite("Bearer $token", userId.toString())
            val result = listSkincare.any { it.skincareId == skincareId }
            result
        } catch (e: Exception) {
            false
        }
    }

    fun getFavorite(): LiveData<Result<List<GetSkincareResponseItem>>> {
        return liveData {
            try {
                val response = apiService.getFavorite("Bearer $token", userId.toString())
                val skincareList = apiService.getAllSkincare("Bearer $token")

                val favSkincareId = response.map { it.skincareId }
                val favSkincare = skincareList.filter { it.skincareId in favSkincareId }
                emit(Result.Success(favSkincare))
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


    suspend fun addFavorite(skincareId: Int): Result<AddFavoriteResponse> {
        return try {
            val request = FavoriteRequest(userId, skincareId)
            val response = apiService.addFavorite("Bearer $token", request)
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

    suspend fun deleteFavorite(skincareId: Int): Result<DeleteFavouriteResponse> {
        return try {
            val request = FavoriteRequest(userId, skincareId)
            val response = apiService.deleteFavorite("Bearer $token", request)
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

    companion object {
        @Volatile
        private var instance: SkincareRepository? = null
        fun getInstance(
            userPref: UserPreference,
            apiService: ApiService,
            dao: SkincareDao
        ): SkincareRepository =
            instance ?: synchronized(this) {
                instance ?: SkincareRepository(userPref, apiService, dao)
            }
    }
}