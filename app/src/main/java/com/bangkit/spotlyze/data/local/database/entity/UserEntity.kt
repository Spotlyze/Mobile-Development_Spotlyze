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
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    @field:ColumnInfo(name = "picture_profile")
    val pictureProfile: String? = null
): Parcelable