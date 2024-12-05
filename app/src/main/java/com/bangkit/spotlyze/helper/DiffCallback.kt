package com.bangkit.spotlyze.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem

internal object DiffCallback : DiffUtil.ItemCallback<GetSkincareResponseItem>() {
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