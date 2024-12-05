package com.bangkit.spotlyze.data.remote.request

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("skincare_id")
    val skincareId: Int? = null
)
