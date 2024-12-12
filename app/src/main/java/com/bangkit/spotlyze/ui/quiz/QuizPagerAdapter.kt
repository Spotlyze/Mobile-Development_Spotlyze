package com.bangkit.spotlyze.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class QuizPagerAdapter(
    private val layouts: List<Int>,
    private val collectData: (Int, View) -> Unit
) : RecyclerView.Adapter<QuizPagerAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(val binding: View) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        collectData(position, holder.binding)
    }

    override fun getItemViewType(position: Int): Int = layouts[position]

    override fun getItemCount(): Int = layouts.size
}
