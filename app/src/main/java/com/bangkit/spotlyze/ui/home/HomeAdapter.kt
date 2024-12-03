package com.bangkit.spotlyze.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.helper.DiffCallback
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ItemLayoutBinding

class HomeAdapter : ListAdapter<GetSkincareResponseItem, HomeAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(skincare: GetSkincareResponseItem) {
            Glide.with(binding.skincareImage.context).load(skincare.skincare_picture)
                .into(binding.skincareImage)
            Log.d("okhttp", "Glide: ${skincare.skincare_picture}")
            binding.tvSkincareName.text = skincare.name
            binding.tvPrice.text = skincare.price.toString()
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
    }
}