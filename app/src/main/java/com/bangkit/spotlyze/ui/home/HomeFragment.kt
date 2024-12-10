package com.bangkit.spotlyze.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.adapter.SkincareAdapter
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.prayatna.spotlyze.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: SkincareAdapter? = null

    private val viewModel: HomeViewModel by viewModels {
        SkincareViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.getAllSkincare().observe(viewLifecycleOwner) { data ->
            when (data) {
                is Result.Loading -> {}
                is Result.Error -> {
                    if (data.error == "Invalid token") {
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    Message.toast(requireActivity(), data.error)
                }

                is Result.Success -> {
                    val skincare = data.data
                    adapter?.submitList(skincare)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = SkincareAdapter()
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(requireActivity())
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
    }
}