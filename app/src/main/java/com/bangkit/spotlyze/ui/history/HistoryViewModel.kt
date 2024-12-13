package com.bangkit.spotlyze.ui.history

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.SkinRepository

class HistoryViewModel(private val repository: SkinRepository) : ViewModel() {

    fun getHistory() = repository.getAllHistory()
    fun getDetailHistory(id: String) = repository.getDetailHistory(id)
    fun getFilteredHistory(id: String) = repository.getFilteredHistory(id)

}