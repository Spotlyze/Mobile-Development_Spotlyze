package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifySkinResponse(

	@field:SerializedName("historyId")
	val historyId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
