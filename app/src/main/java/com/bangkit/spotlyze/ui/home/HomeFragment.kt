package com.bangkit.spotlyze.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.helper.customView.MarginItemDecoration
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.prayatna.spotlyze.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: HomeAdapter? = null

    private val viewModel: HomeViewModel by viewModels {
        UserViewModelFactory.getInstance(requireActivity())
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
        viewModel.getAllSkincare().observe(requireActivity()) { data ->
            when(data) {
                is Result.Loading -> {}
                is Result.Error -> {
                    Message.toast(requireActivity(), data.error)
                    Log.e("okhttp", "homeFragment: ${data.error}")
                }
                is Result.Success -> {
                    val skincare = data.data
                    Log.d("okhttp", "setupViewModel: $skincare")
                    adapter?.submitList(skincare)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = HomeAdapter()
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(MarginItemDecoration(16, 16))
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