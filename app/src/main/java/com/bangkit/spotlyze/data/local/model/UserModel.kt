package com.bangkit.spotlyze.data.local.model

data class UserModel(
    val id: Int,
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)
