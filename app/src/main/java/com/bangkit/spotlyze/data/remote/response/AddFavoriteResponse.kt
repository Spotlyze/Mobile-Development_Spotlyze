package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddFavoriteResponse(

	@field:SerializedName("favoriteId")
	val favoriteId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
