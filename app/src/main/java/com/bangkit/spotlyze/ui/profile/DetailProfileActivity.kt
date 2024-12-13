package com.bangkit.spotlyze.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.R
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
        editUserPictureVM()
        editInfoUserVM()
    }

    private fun editInfoUserVM() {
        viewModel.updateInfoState.observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttpEdit", "error: ${data.error}")
                }

                Result.Loading -> {
                }

                is Result.Success -> {
                    Log.d("okhttpEdit", "success: ${data.data}")
                }
            }
        }
    }

    private fun editUserPictureVM() {
        viewModel.updatePictureState.observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttpEdit", "error: ${data.error}")
                }

                Result.Loading -> {}
                is Result.Success -> {
                    Log.d("okhttpEdit", "success: ${data.data}")
                    finish()
                }
            }

        }
    }

    private fun setupAction() {
        updateUser()
        openGallery()
        backButton()
        logout()
    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.logout))
            builder.setMessage(R.string.are_you_sure)

            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.logOut()
                val intent = Intent(this@DetailProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    private fun backButton() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupProfilePicture() {
        Glide.with(binding.profilePicture.context).load(profilePictureUri)
            .into(binding.profilePicture)
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
            profilePictureUri = it
            Log.d("okhttpEdit", "uri: $profilePictureUri")
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
                updateInfo()
            } else {
                updateInfo()
                finish()
            }
        }
    }

    private fun updateInfo() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        viewModel.updateUserInfo(name, email)
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
                    binding.emailEditText.setText(user.email)
                    binding.nameEditText.setText(user.name)
                    if (user.profilePicture != null)
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