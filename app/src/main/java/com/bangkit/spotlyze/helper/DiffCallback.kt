package com.bangkit.spotlyze.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity

internal object DiffCallback : DiffUtil.ItemCallback<SkincareEntity>() {

    override fun areItemsTheSame(oldItem: SkincareEntity, newItem: SkincareEntity): Boolean {
        return oldItem.skincareId == newItem.skincareId
    }

    override fun areContentsTheSame(oldItem: SkincareEntity, newItem: SkincareEntity): Boolean {
        return oldItem == newItem
    }

}