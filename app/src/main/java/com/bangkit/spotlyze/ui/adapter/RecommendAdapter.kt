package com.bangkit.spotlyze.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.spotlyze.data.remote.response.CleanserItem
import com.bangkit.spotlyze.data.remote.response.MaskItem
import com.bangkit.spotlyze.data.remote.response.MoisturizerItem
import com.bangkit.spotlyze.data.remote.response.TreatmentItem
import com.bangkit.spotlyze.data.source.RecommendationItem
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ItemAnalyzeResultBinding
import com.prayatna.spotlyze.databinding.TitleLayoutBinding

class RecommendationAdapter(private val items: List<RecommendationItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_TREATMENT = 1
        const val VIEW_TYPE_CLEANSER = 2
        const val VIEW_TYPE_MASK = 3
        const val VIEW_TYPE_MOISTURIZER = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecommendationItem.SectionTitle -> VIEW_TYPE_TITLE
            is RecommendationItem.Treatment -> VIEW_TYPE_TREATMENT
            is RecommendationItem.Cleanser -> VIEW_TYPE_CLEANSER
            is RecommendationItem.Mask -> VIEW_TYPE_MASK
            is RecommendationItem.Moisturizer -> VIEW_TYPE_MOISTURIZER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TITLE -> {
                val binding = TitleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionTitleViewHolder(binding)
            }

            VIEW_TYPE_TREATMENT,
            VIEW_TYPE_CLEANSER,
            VIEW_TYPE_MASK,
            VIEW_TYPE_MOISTURIZER -> {
                val binding = ItemAnalyzeResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecommendationViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RecommendationItem.SectionTitle -> (holder as SectionTitleViewHolder).bind(item)
            is RecommendationItem.Treatment -> (holder as RecommendationViewHolder).bind(item.item)
            is RecommendationItem.Cleanser -> (holder as RecommendationViewHolder).bind(item.item)
            is RecommendationItem.Mask -> (holder as RecommendationViewHolder).bind(item.item)
            is RecommendationItem.Moisturizer -> (holder as RecommendationViewHolder).bind(item.item)
        }
    }

    override fun getItemCount(): Int = items.size

    class SectionTitleViewHolder(private val binding: TitleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendationItem.SectionTitle) {
            binding.tvTitle.text = item.title
        }
    }

    class RecommendationViewHolder(private val binding: ItemAnalyzeResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val productName: TextView = binding.tvSkincareName
        private val productCategory: TextView = binding.skincareCategory

        fun bind(item: Any) {
            val name = when (item) {
                is TreatmentItem -> item.productName
                is CleanserItem -> item.productName
                is MaskItem -> item.productName
                is MoisturizerItem -> item.productName
                else -> throw IllegalArgumentException("Invalid item type")
            }
            val category = when (item) {
                is TreatmentItem -> item.productBrand
                is CleanserItem -> item.productBrand
                is MaskItem -> item.productBrand
                is MoisturizerItem -> item.productBrand
                else -> throw IllegalArgumentException("Invalid item type")
            }
            val imageUrl = when (item) {
                is TreatmentItem -> item.productImageUrl
                is CleanserItem -> item.productImageUrl
                is MaskItem -> item.productImageUrl
                is MoisturizerItem -> item.productImageUrl
                else -> throw IllegalArgumentException("Invalid item type")
            }
            val price = when (item) {
                is TreatmentItem -> item.price
                is CleanserItem -> item.price
                is MaskItem -> item.price
                is MoisturizerItem -> item.price
                else -> throw IllegalArgumentException("Invalid item type")
            }

            val title = when (item) {
                is TreatmentItem -> "Treatment"
                is CleanserItem -> "Cleanser"
                is MaskItem -> "Mask"
                is MoisturizerItem -> "Moisturizer"
                else -> throw IllegalArgumentException("Invalid item type")
            }
            Glide.with(binding.skincareImage.context).load(imageUrl).into(binding.skincareImage)
            productName.text = name
            productCategory.text = category
            binding.skincarePrice.text = price.toString()
            binding.skincareType.text = title
        }
    }
}
