package com.bangkit.spotlyze.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ActivityDetailProfileBinding

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

    }

    private fun setupViewModel() {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.getUserProfile(id.toString()).observe(this) { data ->
            when (data) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    val user = data.data
                    binding.emailEditText.setText(user.email)
                    binding.nameEditText.setText(user.name)
                    Glide.with(binding.profilePicture.context).load(user.profilePicture)
                        .into(binding.profilePicture)
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}