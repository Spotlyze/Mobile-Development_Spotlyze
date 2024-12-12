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
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.adapter.SkincareByTypeAdapter
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.bangkit.spotlyze.ui.skincare.SkincareActivity
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: SkincareByTypeAdapter? = null

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
        setupAction()
    }

    private fun setupAction() {
        goToSkincare()
        stickyView()
    }

    private fun stickyView() {
        val stickyLayout = binding.stickyLayout
        val nestedScrollView = binding.nestedScrollView

        nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = nestedScrollView.scrollY
            val stickyLayoutTop = stickyLayout.top

            if (scrollY >= stickyLayoutTop) {
                stickyLayout.elevation = resources.getDimension(R.dimen.margin)
                stickyLayout.translationY = (scrollY - stickyLayoutTop).toFloat()
            } else {
                stickyLayout.elevation = resources.getDimension(R.dimen.zero)
                stickyLayout.translationY = 0f
            }
        }
    }

    private fun goToSkincare() {
        binding.btnSkincare.setOnClickListener {
            val intent = Intent(requireActivity(), SkincareActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        viewModel.getAllSkincare().observe(viewLifecycleOwner) { data ->
            when (data) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    if (data.error == "Invalid token") {
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    Message.toast(requireActivity(), data.error)
                }

                is Result.Success -> {
                    showLoading(false)
                    val skincare = processData(data.data)
                    Log.d("okhttp", "$skincare")
                    adapter?.submitList(skincare)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSkincare.visibility = View.GONE
            binding.tvExploreSkincare.visibility = View.GONE
            binding.tvMadeInIndonesia.visibility = View.GONE
            binding.cardView.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSkincare.visibility = View.VISIBLE
            binding.tvExploreSkincare.visibility = View.VISIBLE
            binding.tvMadeInIndonesia.visibility = View.VISIBLE
            binding.cardView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun processData(skincareList: List<SkincareEntity>): List<SkincareByTypeAdapter.SkincareItem> {
        val result = mutableListOf<SkincareByTypeAdapter.SkincareItem>()
        val groupedByCategory = skincareList.groupBy { it.category }

        groupedByCategory.forEach { (category, items) ->
            result.add(SkincareByTypeAdapter.SkincareItem.Header(category ?: "Unknown"))
            result.addAll(
                items.take(5).map { SkincareByTypeAdapter.SkincareItem.Item(it) }
            )
        }

        return result
    }




    private fun setupAdapter() {
        adapter = SkincareByTypeAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(requireActivity())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
