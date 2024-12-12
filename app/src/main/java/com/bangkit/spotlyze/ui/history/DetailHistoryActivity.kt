package com.bangkit.spotlyze.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.bangkit.spotlyze.ui.home.HomeAdapter
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private val viewModel: HistoryViewModel by viewModels {
        SkinViewModelFactory.getInstance(this)
    }
    private var adapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupAdapter()
        setupSkincare()
    }

    private fun setupAdapter() {
        adapter = HomeAdapter()
        val layoutManager = GridLayoutManager(this, 2 )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
        binding.recyclerView.layoutManager = layoutManager
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
        val id = intent.getStringExtra(EXTRA_ID)
        viewModel.getDetailHistory(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "detail history: ${data.error}")
                }

                Result.Loading -> {
                    Log.d("okhttp", "detail history: loading")
                }

                is Result.Success -> {
                    val result = data.data
                    Log.d("okhttp", "$result")
                    setupView(result)
                }
            }
        }
    }

    private fun setupSkincare() {
        val id = intent.getStringExtra(EXTRA_ID)
        Log.d("okhttp", "setupSkincare: $id")
        viewModel.getFilteredHistory(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "detail history: ${data.error}")
                }
                Result.Loading -> {
                    showLoading(true)
                    Log.d("okhttp", "detail history: loading")
                }
                is Result.Success -> {
                    showLoading(false)
                    setupViewModel()
                    val result = data.data
                    Log.d("okhttp", "setupSkincare: $result")
                    adapter?.submitList(result)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupView(result: List<GetHistoryResponseItem>) {
        binding.tvSkinType.text = result[0].results
        Glide.with(binding.facePicture.context).load(result[0].historyPicture)
            .into(binding.facePicture)
    }
    companion object {
        const val EXTRA_ID = "extra_id"
    }
}