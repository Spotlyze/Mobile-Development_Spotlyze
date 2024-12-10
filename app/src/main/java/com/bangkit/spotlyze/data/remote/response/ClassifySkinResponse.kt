package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifySkinResponse(

	@field:SerializedName("historyId")
	val historyId: Int? = null,

	@field:SerializedName("publicUrl")
	val publicUrl: String? = null,

	@field:SerializedName("predict")
	val predict: String? = null,

	@field:SerializedName("recomendation")
	val recomendation: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
