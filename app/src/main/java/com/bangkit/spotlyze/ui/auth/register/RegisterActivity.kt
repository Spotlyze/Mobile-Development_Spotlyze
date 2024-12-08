package com.bangkit.spotlyze.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bangkit.spotlyze.ui.auth.AuthViewModel
import com.bangkit.spotlyze.ui.auth.login.LoginActivity
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEditText()
        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.registerStatus.observe(this) { status ->
            when(status) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    val user = status.data
                    Log.d("okhttp", "user: $user")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupAction() {
        register()
        goToLogin()
    }

    private fun goToLogin() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun register() {
        binding.btnRegister.setOnClickListener {
            val email = binding.emailInput.emailEditText.text.toString()
            val name = binding.usernameInput.usernameEditText.text.toString()
            val password = binding.passwordInput.passwordEditText.text.toString()
            viewModel.register(
                email = email,
                name = name,
                password = password
            )
        }
    }

    private fun setupEditText() {
        binding.confirmPasswordInput.passwordTextLayout.hint =
            getString(R.string.confirm_password)
    }
}