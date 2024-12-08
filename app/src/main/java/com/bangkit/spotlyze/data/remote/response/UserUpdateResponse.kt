package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserUpdateResponse(

	@field:SerializedName("message")
	val message: String? = null
)
