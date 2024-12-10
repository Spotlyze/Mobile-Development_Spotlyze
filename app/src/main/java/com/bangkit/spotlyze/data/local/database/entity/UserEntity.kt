package com.bangkit.spotlyze.data.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    @field:ColumnInfo(name = "picture_profile")
    val pictureProfile: String
): Parcelable