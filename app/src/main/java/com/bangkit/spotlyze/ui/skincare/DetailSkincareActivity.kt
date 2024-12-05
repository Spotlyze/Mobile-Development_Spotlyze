package com.bangkit.spotlyze.ui.skincare

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivityDetailSkincareBinding

class DetailSkincareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSkincareBinding
    private var id: String? = null
    private val viewModel: SkincareViewModel by viewModels {
        SkincareViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSkincareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()

    }

    private fun setupAction() {
        backButton()
        setupFavoriteState()
    }

    private fun setupFavoriteState() {
        viewModel.addFavoriteState.observe(this) { data ->
            when (data) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    binding.btnFav.setImageResource(R.drawable.ic_fav)
                }
            }
        }
        viewModel.deleteFavoriteState.observe(this) { data ->
            when (data) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    binding.btnFav.setImageResource(R.drawable.ic_unfav)
                }
            }
        }
    }

    private fun backButton() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        id = intent.getStringExtra(EXTRA_ID)
        Log.d("okhttp", "id detail skincare: $id")
        viewModel.getSkincareById(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "error detail skincare:${data.error}")
                }

                Result.Loading -> {}
                is Result.Success -> {
                    val skincare = data.data[0]
                    setupView(skincare)
                    setupFavorite(skincare)
                    updateFavButton(skincare.isFavorite)
                }
            }
        }
    }

    private fun setupFavorite(skincare: SkincareEntity) {
        binding.btnFav.setOnClickListener {
            if (skincare.isFavorite) {
                viewModel.deleteFavorite(skincare.skincareId!!)
                viewModel.deleteFavSkincareDao(skincare)
                skincare.isFavorite = false
            } else {
                viewModel.addFavorite(skincare.skincareId!!)
                viewModel.setFavSkincareDao(skincare)
                skincare.isFavorite = true
            }
        }
        updateFavButton(skincare.isFavorite)
    }

    private fun updateFavButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFav.setImageResource(R.drawable.ic_fav)
        } else {
            binding.btnFav.setImageResource(R.drawable.ic_unfav)
        }
    }


    private fun setupView(skincare: SkincareEntity) {
        binding.tvName.text = skincare.name
        binding.tvDescription.text = skincare.explanation
        binding.tvIngredients.text = skincare.ingredients
        Glide.with(binding.skincarePicture.context).load(skincare.skincarePicture)
            .into(binding.skincarePicture)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}