package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetFavoriteResponseItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("skincare_id")
	val skincareId: Int? = null,

	@field:SerializedName("favorite_id")
	val favoriteId: Int? = null
)
