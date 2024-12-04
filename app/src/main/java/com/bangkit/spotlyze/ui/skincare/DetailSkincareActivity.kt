package com.bangkit.spotlyze.ui.skincare

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.remote.response.GetSkincareResponseItem
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ActivityDetailSkincareBinding

class DetailSkincareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSkincareBinding
    private val viewModel: SkincareViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
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
        val id = intent.getStringExtra(EXTRA_ID)
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
                }
            }
        }
    }

    private fun setupView(skincare: GetSkincareResponseItem) {
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