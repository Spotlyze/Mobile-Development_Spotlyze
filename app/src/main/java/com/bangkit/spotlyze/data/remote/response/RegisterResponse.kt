package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null
)
