package com.bangkit.spotlyze.ui.history

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.bangkit.spotlyze.ui.quiz.AnalyzeActivity
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private val viewModel: HistoryViewModel by viewModels {
        SkinViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        backButton()
    }

    private fun backButton() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        val id = intent.getStringExtra(AnalyzeActivity.CLASSIFY_RESULT)
        viewModel.getDetailHistory(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "detail history: ${data.error}")
                }

                Result.Loading -> {
                    Log.d("okhttp", "detail history: loading")
                }

                is Result.Success -> {
                    Log.d("okhttp", "detail history: ${data.data}")
                    val result = data.data
                    setupView(result)
                }
            }
        }
    }

    private fun setupView(result: List<GetHistoryResponseItem>) {
        binding.tvResult.text = result[0].results
        binding.tvRecommend.text = result[0].recommendation
        Glide.with(binding.imageProfile.context).load(result[0].historyPicture)
            .into(binding.imageProfile)
    }
}