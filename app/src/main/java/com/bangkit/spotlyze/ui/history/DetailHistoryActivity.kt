package com.bangkit.spotlyze.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.remote.response.GetHistoryResponseItem
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.bangkit.spotlyze.ui.home.HomeAdapter
import com.bangkit.spotlyze.ui.main.MainActivity
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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val toMain = intent.getBooleanExtra("navigation", false)

                if (toMain) {
                    val intent = Intent(this@DetailHistoryActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupAdapter() {
        adapter = HomeAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun setupAction() {
        backButton()
    }

    private fun backButton() {
        binding.toolBar.setNavigationOnClickListener {
            val toMain = intent.getBooleanExtra("navigation", false)
            Log.d("okhttp", "backButton: $toMain")
            if (toMain) {
                val intent = Intent(this@DetailHistoryActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }
    }

    private fun setupViewModel() {
        val id = intent.getStringExtra(EXTRA_ID)
        viewModel.getDetailHistory(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    showLoading(false)
                    binding.progressBar.visibility = View.GONE
                }

                Result.Loading -> {
                    showLoading(true)
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    showLoading(false)
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
                    showLoading(false)
                    Message.offlineDialog(this) {
                        finish()
                    }
                }

                Result.Loading -> {
                    showLoading(true)
                    Log.d("okhttp", "detail history: loading")
                }

                is Result.Success -> {
                    showLoading(false)
                    setupViewModel()
                    val result = data.data
                    adapter?.submitList(result)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.tvSkinStatus.visibility = View.GONE
            binding.dummyPicture.visibility = View.GONE
            binding.tvRecommendations.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.dummyPicture.visibility = View.VISIBLE
            binding.tvSkinStatus.visibility = View.VISIBLE
            binding.tvRecommendations.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.facePicture.visibility = View.VISIBLE
        }
    }

    private fun setupView(result: List<GetHistoryResponseItem>) {
        binding.dummyPicture.visibility = View.GONE
        binding.tvSkinStatus.text = when (result[0].results) {
            "dark circle" -> "Your skin status is dark circle"
            "acne" -> "Your skin status is acne"
            "wrinkle" -> "Your skin status is wrinkle"
            "normal" -> "Your skin status is normal"
            else -> "Your skin status is unknown"
        }
        Glide.with(binding.facePicture.context).load(result[0].historyPicture)
            .into(binding.facePicture)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}