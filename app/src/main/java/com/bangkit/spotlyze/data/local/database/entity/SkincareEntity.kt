package com.bangkit.spotlyze.data.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "skincare")
@Parcelize
data class SkincareEntity(
    @field:ColumnInfo(name = "skincare_id")
    @field:PrimaryKey(autoGenerate = true)
    val skincareId: Int? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    val brand: String? = null,

    val category: String? = null,

    val rating: Int? = null,

    @field:ColumnInfo(name = "is_recommend")
    val isRecommend: Boolean? = null,

    val description: String? = null,

    @field:ColumnInfo(name = "skin_type")
    val skinType: String? = null,

    @field:ColumnInfo(name = "type")
    val type: String? = null,

    @field:ColumnInfo(name = "price")
    val price: Int? = null,

    @field:ColumnInfo(name = "skincare_picture")
    val skincarePicture: String? = null,

    @field:ColumnInfo(name = "ingredients")
    val ingredients: String? = null,

    @field:ColumnInfo(name = "explanation")
    val explanation: String? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean = false,
) : Parcelable