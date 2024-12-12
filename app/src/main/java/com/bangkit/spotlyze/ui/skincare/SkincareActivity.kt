package com.bangkit.spotlyze.ui.skincare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.spotlyze.data.local.database.entity.SkincareEntity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.data.source.SortType
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.helper.customView.BoundEdgeEffect
import com.bangkit.spotlyze.ui.SkincareViewModelFactory
import com.bangkit.spotlyze.ui.adapter.SkincareAdapter
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivitySkincareBinding
import java.util.Locale

class SkincareActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySkincareBinding

    private val viewModel: SkincareViewModel by viewModels {
        SkincareViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: SkincareAdapter
    private lateinit var dataList: ArrayList<SkincareEntity>
    private lateinit var searchList: ArrayList<SkincareEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkincareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupViewModel()
        setupMenu()
    }

    private fun setupMenu() {
        binding.btnFilter.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                handleFilterSelection(menuItem)
                popupMenu.dismiss()
                true
            }
            popupMenu.show()
        }
    }

    private fun handleFilterSelection(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.sort_random -> {
                viewModel.changeSortType(SortType.RANDOM)
            }

            R.id.sort_desc -> {
                viewModel.changeSortType(SortType.DESCENDING)
            }

            R.id.sort_asc -> {
                viewModel.changeSortType(SortType.ASCENDING)
            }
        }
    }

    private fun setupSearchView(data: List<SkincareEntity>) {
        searchList = ArrayList(data)
        dataList = ArrayList(data)

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrEmpty()) {
                    binding.recyclerView.scrollToPosition(0)
                }

                searchList.clear()
                val searchText = newText?.lowercase(Locale.getDefault()) ?: ""
                if (searchText.isNotEmpty()) {
                    dataList.forEach {
                        if (it.name?.lowercase(Locale.getDefault())?.contains(searchText) == true) {
                            searchList.add(it)
                        }
                    }
                } else {
                    searchList.addAll(dataList)
                }

                if (searchList.isNotEmpty()) {
                    adapter.submitList(ArrayList(searchList))
                } else {
                    adapter.submitList(emptyList())
                }
                return false
            }
        })
    }

    private fun setupViewModel() {
        viewModel.getAllSkincare().observe(this) { data ->
            when (data) {
                is Result.Loading -> {
                    Log.d("okhttp", "loading")
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    Message.offlineDialog(this)
                    binding.progressBar.visibility = View.GONE
                    if (data.error == "Invalid token") {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Message.toast(this, data.error)
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val skincare = data.data.take(10)
                    dataList = ArrayList(skincare)
                    setupSearchView(skincare)
                    adapter.submitList(skincare)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = SkincareAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
    }

}