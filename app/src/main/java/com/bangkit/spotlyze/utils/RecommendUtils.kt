package com.bangkit.spotlyze.utils

import com.bangkit.spotlyze.data.remote.response.Recommend
import com.bangkit.spotlyze.data.source.RecommendationItem

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
