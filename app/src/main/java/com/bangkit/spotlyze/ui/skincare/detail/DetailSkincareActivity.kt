package com.bangkit.spotlyze.ui.skincare.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.skincare.SkincareViewModel
import com.bangkit.spotlyze.utils.formatToRupiah
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

    }


    private fun backButton() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        id = intent.getStringExtra(EXTRA_ID)
        viewModel.getSkincareById(id!!).observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Message.offlineDialog(this) {
                        setupViewModel()
                    }
                    showLoading(false)
                }
                Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val skincare = data.data[0]
                    setupView(skincare)
                    setupFavorite(skincare.skincareId!!)
                    updateFavButton(skincare.isFavorite)
                    setupInfo(skincare)
                }
            }
        }
    }

    private fun setupInfo(skincare: SkincareEntity) {
        binding.btnIngredients.setOnClickListener {
            Message.bottomSheetDialog(this,"Ingredients", skincare.ingredients!!)
        }
        binding.btnHowToUse.setOnClickListener {
            Message.bottomSheetDialog(this, "How to use", skincare.description!!)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupFavorite(skincareId: Int) {
        viewModel.isFavoriteSkincare(skincareId)
        viewModel.isFavorite.observe(this) { isFavorite ->
            updateFavButton(isFavorite)
        }

        binding.btnFav.setOnClickListener {
            if (viewModel.isFavorite.value == true) {
                viewModel.deleteFavorite(skincareId)
            } else {
                viewModel.addFavorite(skincareId)
            }
        }
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
        binding.tvBrand.text = skincare.brand
        binding.tvCategory.text = skincare.category
        binding.tvSkinType.text = skincare.skinType
        binding.tvPrice.text = formatToRupiah(skincare.price!!)
        Glide.with(binding.skincarePicture.context).load(skincare.skincarePicture)
            .into(binding.skincarePicture)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}