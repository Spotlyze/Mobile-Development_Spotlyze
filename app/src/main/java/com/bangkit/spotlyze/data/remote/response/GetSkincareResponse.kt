package com.bangkit.spotlyze.data.remote.response

data class GetSkincareResponse(
	val getSkincareResponse: List<GetSkincareResponseItem?>? = null
)

data class GetSkincareResponseItem(
	val skincare_picture: String? = null,
	val price: Int? = null,
	val name: String? = null,
	val ingredients: String? = null,
	val skincareId: Int? = null,
	val type: String? = null,
	val explanation: String? = null
)

