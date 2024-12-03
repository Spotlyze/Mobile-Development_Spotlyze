package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetSkincareResponseItem(

	@field:SerializedName("skincare_picture")
	val skincarePicture: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("skincare_id")
	val skincareId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("explanation")
	val explanation: String? = null
)
