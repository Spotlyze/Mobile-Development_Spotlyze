package com.bangkit.spotlyze.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupCamera()
        checkDarkMode()
    }

    private fun checkDarkMode() {
        val isDarkMode = viewModel.getCurrentTheme()
        checkDarkSetting(isDarkMode)
        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            checkDarkSetting(isDarkModeActive)
        }
    }

    private fun setupCamera() {
        binding.floatingCameraButton.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_camera)
        }
    }

    private fun checkDarkSetting(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupView() {
        val navView: BottomNavigationView = binding.bottomNavView
        navView.itemIconTintList = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.navigation_camera) {
                navView.visibility = View.GONE
                binding.floatingCameraButton.visibility = View.GONE
                binding.bottomNavContainer.visibility = View.GONE
                binding.navHostFragment.layoutParams =
                    (binding.navHostFragment.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        bottomMargin = 0
                    }
            } else {
                navView.visibility = View.VISIBLE
                binding.floatingCameraButton.visibility = View.VISIBLE
                binding.bottomNavContainer.visibility = View.VISIBLE
            }
        }
    }
}
