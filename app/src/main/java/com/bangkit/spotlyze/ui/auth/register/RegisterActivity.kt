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
        setupLogic()
    }

    private fun setupLogic() {
        if (binding.passwordInput.passwordEditText.text.toString() != binding.confirmPasswordInput.passwordEditText.text.toString()) {
            binding.confirmPasswordInput.passwordTextLayout.error =
                getString(R.string.password_not_match)
        }
    }

    private fun isPasswordMatch(): Boolean {
        return binding.passwordInput.passwordEditText.text.toString() == binding.confirmPasswordInput.passwordEditText.text.toString()
    }

    private fun setupViewModel() {
        viewModel.registerStatus.observe(this) { status ->
            when (status) {
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
        goToLogin()
        validateRegister()
    }

    private fun validateRegister() {
        if (isPasswordMatch()) {
            register()
        } else {
            binding.confirmPasswordInput.passwordTextLayout.error =
                getString(R.string.password_not_match)
        }
    }

    private fun goToLogin() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
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