package com.bangkit.spotlyze.ui.skincare.favourite

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.home.HomeAdapter
import com.bangkit.spotlyze.ui.skincare.SkincareViewModel
import com.prayatna.spotlyze.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private var adapter: HomeAdapter? = null
    private val viewModel: SkincareViewModel by viewModels {
        SkincareViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAdapter()
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

    private fun setupAdapter() {
            adapter = HomeAdapter()
            val layoutManager = GridLayoutManager(this, 2 )
            binding.recyclerView.adapter = adapter
            binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
            binding.recyclerView.layoutManager = layoutManager
        }

    private fun setupViewModel() {
        viewModel.getFavorite().observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Message.toast(this, data.error)
                }
                Result.Loading -> { }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter?.submitList(data.data)
                    if (data.data.isEmpty()) {
                        binding.errorImage.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
    }
}