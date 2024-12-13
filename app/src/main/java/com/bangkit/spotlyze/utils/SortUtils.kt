package com.bangkit.spotlyze.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import com.bangkit.spotlyze.data.source.SortType

object SortUtils {
    fun getSortedQuery(sortType: SortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM skincare ")
        when (sortType) {
            SortType.RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
            SortType.MOISTURIZER -> {
                simpleQuery.append("WHERE category = 'Moisturizer'")
            }
            SortType.CLEANSER -> {
                simpleQuery.append("WHERE category = 'Cleanser'")
            }
            SortType.MASK -> {
                simpleQuery.append("WHERE category = 'Mask'")
            }
            SortType.TREATMENT -> {
                simpleQuery.append("WHERE category = 'Treatment'")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}