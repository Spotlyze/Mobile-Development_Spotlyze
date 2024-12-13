package com.bangkit.spotlyze.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.helper.SkinDiffCallback
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.HistoryItemLayoutBinding

class HistoryAdapter :
    ListAdapter<GetHistoryResponseItem, HistoryAdapter.ViewHolder>(SkinDiffCallback) {
    class ViewHolder(private val binding: HistoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GetHistoryResponseItem) {
            Glide.with(binding.imageResult.context).load(result.historyPicture)
                .into(binding.imageResult)
            binding.tvResult.text = result.results
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HistoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailHistoryActivity::class.java)
            intent.putExtra(DetailHistoryActivity.EXTRA_ID, item.analyzeHistoryId.toString())
            holder.itemView.context.startActivity(intent)
        }
    }
}