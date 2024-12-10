package com.bangkit.spotlyze.data.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "history")
@Parcelize
class HistoryEntity (
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val historyId: Int,
    @field:ColumnInfo(name = "user_id")
    val idUser: Int,
    val results: String,
    val recommendation: String,
    @field:ColumnInfo(name = "history_picture")
    val historyPicture: String
): Parcelable