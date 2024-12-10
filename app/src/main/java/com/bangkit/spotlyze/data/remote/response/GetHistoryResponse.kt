package com.bangkit.spotlyze.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("GetHistoryResponse")
	val getHistoryResponse: List<GetHistoryResponseItem?>? = null
)

data class GetHistoryResponseItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("analyze_history_id")
	val analyzeHistoryId: Int? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("results")
	val results: String? = null,

	@field:SerializedName("history_picture")
	val historyPicture: String? = null
)
