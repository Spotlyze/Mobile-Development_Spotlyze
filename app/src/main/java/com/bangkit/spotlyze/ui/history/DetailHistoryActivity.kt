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
import com.prayatna.spotlyze.R
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
                    binding.dummyPicture.visibility = View.GONE
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
            binding.tvRecommend.visibility = View.GONE
            binding.tvSkinStatus.visibility = View.GONE
            binding.tvSkinCond.visibility = View.GONE
            binding.tvSkinStatus.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.tvRecommend.visibility = View.VISIBLE
            binding.tvSkinStatus.visibility = View.VISIBLE
            binding.tvSkinCond.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.facePicture.visibility = View.VISIBLE
        }
    }

    private fun setupView(result: List<GetHistoryResponseItem>) {
        binding.tvSkinStatus.text = when (result[0].results) {
            "dark circle", "acne", "wrinkle" -> getString(R.string.oh_no)
            "normal" -> getString(R.string.congrats)
            else -> getString(R.string.unknown)
        }

        binding.tvSkinCond.text = when (result[0].results) {
            "dark circle" -> getString(R.string.you_have_a_dark_circle)
            "normal" -> getString(R.string.your_skin_is_normal)
            "wrinkle" -> getString(R.string.you_have_wrinkles)
            "acne" -> getString(R.string.you_have_acne)
            else -> getString(R.string.unknown)
        }
        Glide.with(binding.facePicture.context).load(result[0].historyPicture)
            .into(binding.facePicture)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}