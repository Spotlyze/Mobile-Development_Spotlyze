package com.bangkit.spotlyze.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.bangkit.spotlyze.ui.history.HistoryActivity
import com.bangkit.spotlyze.ui.skincare.favourite.FavoriteActivity
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupViewModel() {
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            setupProfile(user.id)
            goToDetailProfile(user.id)
        }
    }

    private fun setupProfile(id: Int) {
        viewModel.getUserProfile(id.toString()).observe(viewLifecycleOwner) { user ->
            when (user) {
                is Result.Error -> {
                    if (user.error == "Invalid token") {
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    Message.toast(requireActivity(), user.error)
                }

                is Result.Loading -> {}
                is Result.Success -> {
                    val data = user.data
                    binding.tvUserName.text = data.name
                    binding.tvEmail.text = data.email
                    if (data.profilePicture != null) {
                        Glide.with(binding.userProfileImage.context).load(data.profilePicture)
                            .into(binding.userProfileImage)
                    }
                    Log.d("okhttp", "profile fetched: $data")
                }
            }
        }
    }

    private fun setupAction() {
        goToFavourite()
        goToHistory()
        darkMode()
        getTheme()
    }

    private fun getTheme() {
        viewModel.getThemeSetting().observe(viewLifecycleOwner) {
            checkDarkSetting(it)
        }
    }

    private fun darkMode() {
        binding.settingsView.btnDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setThemeSetting(isChecked)
        }
    }

    private fun goToHistory() {
        binding.btnHistory.setOnClickListener {
            val intent = Intent(requireActivity(), HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToFavourite() {
        binding.btnFavourite.setOnClickListener {
            val intent = Intent(requireActivity(), FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToDetailProfile(id: Int) {
        binding.btnProfileDetail.setOnClickListener {
            val intent = Intent(requireActivity(), DetailProfileActivity::class.java)
            intent.putExtra(DetailProfileActivity.EXTRA_ID, id)
            startActivity(intent)
        }
    }

    private fun checkDarkSetting(isDark: Boolean) {
        binding.settingsView.btnDarkMode.isChecked = isDark
    }

    override fun onStart() {
        super.onStart()
        setupViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}