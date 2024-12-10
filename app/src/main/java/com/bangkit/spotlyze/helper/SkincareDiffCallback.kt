package com.bangkit.spotlyze.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.remote.response.Recommend

internal object SkincareDiffCallback : DiffUtil.ItemCallback<GetSkincareResponseItem>() {
    override fun areItemsTheSame(
        oldItem: GetSkincareResponseItem,
        newItem: GetSkincareResponseItem
    ): Boolean {
        return oldItem.skincareId == newItem.skincareId
    }

    override fun areContentsTheSame(
        oldItem: GetSkincareResponseItem,
        newItem: GetSkincareResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}

internal object SkinDiffCallback : DiffUtil.ItemCallback<GetHistoryResponseItem>() {
    override fun areItemsTheSame(
        oldItem: GetHistoryResponseItem,
        newItem: GetHistoryResponseItem
    ): Boolean {
        return oldItem.analyzeHistoryId == newItem.analyzeHistoryId
    }

    override fun areContentsTheSame(
        oldItem: GetHistoryResponseItem,
        newItem: GetHistoryResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}

internal object SkincareLocalDiffCallback : DiffUtil.ItemCallback<SkincareEntity>() {
    override fun areItemsTheSame(oldItem: SkincareEntity, newItem: SkincareEntity): Boolean {
        return oldItem.skincareId == newItem.skincareId
    }

    override fun areContentsTheSame(oldItem: SkincareEntity, newItem: SkincareEntity): Boolean {
        return oldItem == newItem
    }

}

internal object RecommendDiffUtil : DiffUtil.ItemCallback<Recommend>() {
    override fun areItemsTheSame(oldItem: Recommend, newItem: Recommend): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Recommend, newItem: Recommend): Boolean {
        return oldItem == newItem
    }

}