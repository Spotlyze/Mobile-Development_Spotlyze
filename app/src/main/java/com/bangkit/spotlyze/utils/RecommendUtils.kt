package com.bangkit.spotlyze.utils

import com.bangkit.spotlyze.data.remote.response.Recommend
import com.bangkit.spotlyze.data.source.RecommendationItem
import java.text.NumberFormat
import java.util.Locale
fun flattenRecommendations(recommend: Recommend): List<RecommendationItem> {
    val items = mutableListOf<RecommendationItem>()

    recommend.treatment?.forEach { item ->
        item?.let { items.add(RecommendationItem.Treatment(it)) }
    }

    recommend.cleanser?.forEach { item ->
        item?.let { items.add(RecommendationItem.Cleanser(it)) }
    }

    recommend.mask?.forEach { item ->
        item?.let { items.add(RecommendationItem.Mask(it)) }
    }

    recommend.moisturizer?.forEach { item ->
        item?.let { items.add(RecommendationItem.Moisturizer(it)) }
    }

    return items
}

fun formatToRupiah(amount: Int): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(amount).replace("Rp", "Rp ") // Ensure space after Rp
}

