package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetSkincareResponseItem(

	@field:SerializedName("skincare_picture")
	val skincarePicture: String? = null,

	@field:SerializedName("description_processed")
	val descriptionProcessed: String? = null,

	@field:SerializedName("skincare_id")
	val skincareId: Int? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("concern")
	val concern: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("is_recommend")
	val isRecommend: Int? = null,

	@field:SerializedName("star_rating")
	val starRating: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("subcategory")
	val subcategory: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null
)
