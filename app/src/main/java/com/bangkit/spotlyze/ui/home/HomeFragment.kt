package com.bangkit.spotlyze.ui.home

import android.content.Intent
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
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.bangkit.spotlyze.ui.quiz.QuizActivity
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: HomeAdapter? = null

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
        setupTest()
    }

    private fun setupTest() {
        binding.btnProfile.setOnClickListener {
            val intent = Intent(requireActivity(), QuizActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        viewModel.getAllSkincare().observe(viewLifecycleOwner) { data ->
            when(data) {
                is Result.Loading -> {}
                is Result.Error -> {
                    if (data.error == "Invalid token") {
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    Message.toast(requireActivity(), data.error)
                    Log.e("okhttp", "homeFragment: ${data.error}")
                }
                is Result.Success -> {
                    val skincare = data.data.take(5)
                    Log.d("okhttp", "setupViewModel: $skincare")
                    adapter?.submitList(skincare)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = HomeAdapter()
        val marginStart = resources.getDimensionPixelSize(R.dimen.margin)
        val marginEnd = resources.getDimensionPixelSize(R.dimen.margin)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(MarginItemDecoration(marginStart, marginEnd))
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