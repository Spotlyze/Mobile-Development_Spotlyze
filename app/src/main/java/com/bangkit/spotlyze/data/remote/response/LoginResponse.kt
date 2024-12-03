package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
