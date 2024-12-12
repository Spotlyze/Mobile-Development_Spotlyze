package com.bangkit.spotlyze.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.ui.skincare.detail.DetailSkincareActivity
import com.bangkit.spotlyze.utils.formatToRupiah
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ItemAnalyzeResultBinding
import com.prayatna.spotlyze.databinding.TitleLayoutBinding

class SkincareByTypeAdapter :
    ListAdapter<SkincareByTypeAdapter.SkincareItem, RecyclerView.ViewHolder>(
        SkincareByTypeDiffCallback
    ) {

    sealed class SkincareItem {
        data class Header(val title: String) : SkincareItem()
        data class Item(val skincare: SkincareEntity) : SkincareItem()
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SkincareItem.Header -> VIEW_TYPE_HEADER
            is SkincareItem.Item -> VIEW_TYPE_ITEM
        }
    }

    inner class HeaderViewHolder(private val binding: TitleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: SkincareItem.Header) {
            binding.tvTitle.text = header.title
        }
    }

    inner class ItemViewHolder(private val binding: ItemAnalyzeResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SkincareItem.Item) {
            val skincare = item.skincare
            Glide.with(binding.skincareImage.context).load(skincare.skincarePicture)
                .into(binding.skincareImage)
            binding.tvSkincareName.text = skincare.name
            binding.skincareType .text = skincare.type
            binding.skincarePrice.text = formatToRupiah(skincare.price!!)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailSkincareActivity::class.java)
                intent.putExtra(DetailSkincareActivity.EXTRA_ID, skincare.skincareId.toString())
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = TitleLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_ITEM -> {
                val binding = ItemAnalyzeResultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as SkincareItem.Header)
            is ItemViewHolder -> holder.bind(getItem(position) as SkincareItem.Item)
        }
    }
}

object SkincareByTypeDiffCallback : DiffUtil.ItemCallback<SkincareByTypeAdapter.SkincareItem>() {
    override fun areItemsTheSame(
        oldItem: SkincareByTypeAdapter.SkincareItem,
        newItem: SkincareByTypeAdapter.SkincareItem
    ): Boolean {
        return when {
            oldItem is SkincareByTypeAdapter.SkincareItem.Header &&
                    newItem is SkincareByTypeAdapter.SkincareItem.Header -> oldItem.title == newItem.title
            oldItem is SkincareByTypeAdapter.SkincareItem.Item &&
                    newItem is SkincareByTypeAdapter.SkincareItem.Item -> oldItem.skincare.skincareId == newItem.skincare.skincareId
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: SkincareByTypeAdapter.SkincareItem,
        newItem: SkincareByTypeAdapter.SkincareItem
    ): Boolean {
        return oldItem == newItem
    }
}
