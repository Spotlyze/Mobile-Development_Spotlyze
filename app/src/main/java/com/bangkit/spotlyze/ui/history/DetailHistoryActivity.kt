package com.bangkit.spotlyze.ui.history

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.prayatna.spotlyze.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private val viewModel: HistoryViewModel by viewModels{
        SkinViewModelFactory.getInstance(this)
    }
    private val index = intent.getIntExtra(EXTRA_INDEX, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.getDetailHistory(index).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "detail history: ${data.error}")
                }
                Result.Loading -> {}
                is Result.Success -> {
                    binding.tvTest.text = data.data.results.toString()
                }
            }
        }
    }

    companion object {
        const val EXTRA_INDEX = "extra_index"
    }
}