package com.bangkit.spotlyze.utils

import com.bangkit.spotlyze.data.remote.response.Recommend
import com.bangkit.spotlyze.data.source.RecommendationItem

fun flattenRecommendations(recommend: Recommend): List<RecommendationItem> {
    val items = mutableListOf<RecommendationItem>()

    items.add(RecommendationItem.SectionTitle("Treatments"))
    recommend.treatment?.forEach { item ->
        item?.let { items.add(RecommendationItem.Treatment(it)) }
    }

    items.add(RecommendationItem.SectionTitle("Cleansers"))
    recommend.cleanser?.forEach { item ->
        item?.let { items.add(RecommendationItem.Cleanser(it)) }
    }

    items.add(RecommendationItem.SectionTitle("Masks"))
    recommend.mask?.forEach { item ->
        item?.let { items.add(RecommendationItem.Mask(it)) }
    }

    items.add(RecommendationItem.SectionTitle("Moisturizers"))
    recommend.moisturizer?.forEach { item ->
        item?.let { items.add(RecommendationItem.Moisturizer(it)) }
    }

    return items
}
