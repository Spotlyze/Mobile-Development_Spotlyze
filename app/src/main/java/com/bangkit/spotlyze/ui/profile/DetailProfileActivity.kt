package com.bangkit.spotlyze.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.ActivityDetailProfileBinding

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private var profilePictureUri: Uri? = null

    private val viewModel: ProfileViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()

    }

    private fun setupViewModel() {
        getProfileVM()
        editUserVM()
    }

    private fun editUserVM() {
        viewModel.updatePictureState.observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttpEdit", "error: ${data.error}")
                }

                Result.Loading -> {}
                is Result.Success -> {
                    Log.d("okhttpEdit", "success: ${data.data}")
                }
            }

        }
    }

    private fun setupAction() {
        updateUser()
        openGallery()
        setupProfilePicture()
    }

    private fun setupProfilePicture() {
        profilePictureUri?.let {
            Glide.with(binding.profilePicture.context).load(profilePictureUri)
                .into(binding.profilePicture)
        }
    }

    private fun openGallery() {
        binding.btnEditImage.setOnClickListener {
            startGallery()
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d("pictureProfile", "Selected URI: $it")
            Log.d("pictureProfile", "URI: $profilePictureUri")
            profilePictureUri = it
            setupProfilePicture()
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun updateUser() {
        binding.btnEditUser.setOnClickListener {
            if (profilePictureUri != null) {
                updateProfilePicture()
            }
            updateInfo()
        }
    }

    private fun updateInfo() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        viewModel.updateUserInfo(name, email, this)
    }

    private fun updateProfilePicture() {
        viewModel.updateProfilePicture(profilePictureUri!!, this)
    }

    private fun getProfileVM() {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.getUserProfile(id.toString()).observe(this) { data ->
            when (data) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    val user = data.data
                    Log.d("okhttp", "profile picture: $profilePictureUri")
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