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
import com.bangkit.spotlyze.ui.skincare.favourite.FavoriteActivity
import com.bumptech.glide.Glide
import com.prayatna.spotlyze.R
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

        setupView()
        setupAction()
        setupViewModel()
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
                    Message.toast(requireActivity(), user.error)
                }

                is Result.Loading -> {}
                is Result.Success -> {
                    val data = user.data
                    binding.tvUserName.text = data.name
                    binding.tvEmail.text = data.email
                    if (!data.profilePicture.isNullOrEmpty()) {
                        Glide.with(binding.userProfileImage.context).load(data.profilePicture)
                            .into(binding.userProfileImage)
                    } else {
                        binding.userProfileImage.setImageResource(R.drawable.dummy_profile)
                    }
                    Log.d("okhttp", "setupProfile: ${data.profilePicture}")
                }
            }
        }
    }

    private fun setupAction() {
        goToHistory()
        goToFavourite()
    }

    private fun goToFavourite() {
        binding.btnFavourite.setOnClickListener {
            val intent = Intent(requireActivity(), FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToHistory() {
    }

    private fun goToDetailProfile(id: Int) {
        binding.btnProfileDetail.setOnClickListener {
            val intent = Intent(requireActivity(), DetailProfileActivity::class.java)
            intent.putExtra(DetailProfileActivity.EXTRA_ID, id)
            startActivity(intent)
        }
    }

    private fun setupView() {
        binding.actionBar.btnSearch.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}