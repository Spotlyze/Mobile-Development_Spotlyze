package com.bangkit.spotlyze.data.local.model

data class UserModel(
    val name: String,
    val password: String,
    val isLogin: Boolean = false
)
