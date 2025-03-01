package com.bangkit.spotlyze.ui.skincare

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Menu
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
    private var selectedMenuItemId = R.id.sort_random

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

            updateMenuItemStyle(popupMenu.menu, selectedMenuItemId)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId != selectedMenuItemId) {
                    menuItem.isChecked = true
                    selectedMenuItemId = menuItem.itemId
                    handleFilterSelection(menuItem)
                }
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

            R.id.moisturizer -> {
                viewModel.changeSortType(SortType.MOISTURIZER)
            }

            R.id.treatment -> {
                viewModel.changeSortType(SortType.TREATMENT)
            }

            R.id.cleanser -> {
                viewModel.changeSortType(SortType.CLEANSER)
            }

            R.id.mask -> {
                viewModel.changeSortType(SortType.MASK)
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
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    if (data.error == "Invalid token") {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Message.offlineDialog(this) {
                            setupViewModel()
                        }
                    }
                    Message.toast(this, data.error)
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val skincare = data.data
                    dataList = ArrayList(skincare)
                    setupSearchView(skincare)
                    adapter.submitList(skincare)
                }
            }
        }
    }

    private fun updateMenuItemStyle(menu: Menu, selectedItemId: Int) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val spannableTitle = SpannableString(item.title)
            if (item.itemId == selectedItemId) {
                spannableTitle.setSpan(StyleSpan(Typeface.BOLD), 0, spannableTitle.length, 0)
            } else {
                spannableTitle.setSpan(StyleSpan(Typeface.NORMAL), 0, spannableTitle.length, 0)
            }
            item.title = spannableTitle
        }
    }

    private fun setupAdapter() {
        val layoutManager = GridLayoutManager(this, 2)
        val scrollPosition = layoutManager.findFirstVisibleItemPosition()
        layoutManager.scrollToPosition(scrollPosition)
        adapter = SkincareAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.edgeEffectFactory = BoundEdgeEffect(this)
        binding.recyclerView.layoutManager = layoutManager
    }
}