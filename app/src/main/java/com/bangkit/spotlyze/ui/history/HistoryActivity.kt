package com.bangkit.spotlyze.ui.history

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.prayatna.spotlyze.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private var adapter: HistoryAdapter? = null
    private val viewModel: HistoryViewModel by viewModels {
        SkinViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        backButtonPressed()
    }

    private fun backButtonPressed() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        viewModel.getHistory().observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "setupViewModel: ${data.error}")
                }

                Result.Loading -> {

                }

                is Result.Success -> {
                    val history = data.data
                    Log.d("okhttp", "histories: $history")
                    adapter?.submitList(history)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = HistoryAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
        binding.recyclerView.layoutManager = layoutManager
    }
}