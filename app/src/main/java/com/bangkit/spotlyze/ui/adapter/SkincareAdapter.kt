package com.bangkit.spotlyze.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.helper.SkincareLocalDiffCallback
import com.bangkit.spotlyze.ui.skincare.DetailSkincareActivity
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ItemLayoutBinding

class SkincareAdapter : ListAdapter<SkincareEntity, SkincareAdapter.ViewHolder>(SkincareLocalDiffCallback) {
    class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(skincare: SkincareEntity) {
            Glide.with(binding.skincareImage.context).load(skincare.skincarePicture)
                .into(binding.skincareImage)
            binding.tvSkincareName.text = skincare.name
            binding.tvProcess.text = skincare.ingredients
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailSkincareActivity::class.java)
            intent.putExtra(DetailSkincareActivity.EXTRA_ID, item.skincareId.toString())
            holder.itemView.context.startActivity(intent)
        }
    }
}