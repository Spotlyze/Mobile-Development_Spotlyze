package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
