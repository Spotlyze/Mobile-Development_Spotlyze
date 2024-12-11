package com.bangkit.spotlyze.data.source

import android.os.Parcelable
import com.bangkit.spotlyze.data.remote.response.CleanserItem
import com.bangkit.spotlyze.data.remote.response.MaskItem
import com.bangkit.spotlyze.data.remote.response.MoisturizerItem
import com.bangkit.spotlyze.data.remote.response.TreatmentItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class RecommendationItem : Parcelable {
//    @Parcelize
//    data class SectionTitle(val title: String) : RecommendationItem()

    @Parcelize
    data class Treatment(val item: @RawValue TreatmentItem) : RecommendationItem()

    @Parcelize
    data class Cleanser(val item: @RawValue CleanserItem) : RecommendationItem()

    @Parcelize
    data class Mask(val item: @RawValue MaskItem) : RecommendationItem()

    @Parcelize
    data class Moisturizer(val item: @RawValue MoisturizerItem) : RecommendationItem()
}

@Parcelize
data class TreatmentItem(
    val productName: String,
    val productBrand: String,
    val productImageUrl: String
) : Parcelable

@Parcelize
data class CleanserItem(
    val productName: String,
    val productBrand: String,
    val productImageUrl: String
) : Parcelable

@Parcelize
data class MaskItem(
    val productName: String,
    val productBrand: String,
    val productImageUrl: String
) : Parcelable

@Parcelize
data class MoisturizerItem(
    val productName: String,
    val productBrand: String,
    val productImageUrl: String
) : Parcelable
