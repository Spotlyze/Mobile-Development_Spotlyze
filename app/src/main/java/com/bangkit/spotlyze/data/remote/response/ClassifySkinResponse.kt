package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifySkinResponse(

	@field:SerializedName("historyId")
	val historyId: Int? = null,

	@field:SerializedName("publicUrl")
	val publicUrl: String? = null,

	@field:SerializedName("predict")
	val predict: String? = null,

	@field:SerializedName("recommend")
	val recommend: Recommend? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class MaskItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("product_brand")
	val productBrand: String? = null,

	@field:SerializedName("product_image_url")
	val productImageUrl: String? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)

data class Recommend(

	@field:SerializedName("Treatment")
	val treatment: List<TreatmentItem?>? = null,

	@field:SerializedName("Cleanser")
	val cleanser: List<CleanserItem?>? = null,

	@field:SerializedName("Mask")
	val mask: List<MaskItem?>? = null,

	@field:SerializedName("Moisturizer")
	val moisturizer: List<MoisturizerItem?>? = null
)

data class CleanserItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("product_brand")
	val productBrand: String? = null,

	@field:SerializedName("product_image_url")
	val productImageUrl: String? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)

data class MoisturizerItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("product_brand")
	val productBrand: String? = null,

	@field:SerializedName("product_image_url")
	val productImageUrl: String? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)

data class TreatmentItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("product_brand")
	val productBrand: String? = null,

	@field:SerializedName("product_image_url")
	val productImageUrl: String? = null,

	@field:SerializedName("skin_type")
	val skinType: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)
