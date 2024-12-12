package com.bangkit.spotlyze.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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

    private fun isPasswordMatch(): Boolean {
        val password = binding.passwordInput.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordInput.passwordEditText.text.toString()
        return password == confirmPassword
    }

    private fun isEmailValid(): Boolean {
        val email = binding.emailInput.emailEditText.text.toString()
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(): Boolean {
        val password = binding.passwordInput.passwordEditText.text.toString()
        return password.length >= 8
    }

    private fun setupViewModel() {
        viewModel.registerStatus.observe(this) { status ->
            when (status) {
                is Result.Error -> {
                }
                Result.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is Result.Success -> {
                    val user = status.data
                    Log.d("okhttp", "user: $user")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupAction() {
        goToLogin()
        setupRegisterButton()
        validateFields()
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                val email = binding.emailInput.emailEditText.text.toString()
                val name = binding.usernameInput.usernameEditText.text.toString()
                val password = binding.passwordInput.passwordEditText.text.toString()

                viewModel.register(email = email, name = name, password = password)
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (!isPasswordMatch()) {
            binding.confirmPasswordInput.passwordTextLayout.error = getString(R.string.password_not_match)
            isValid = false
        } else {
            binding.confirmPasswordInput.passwordTextLayout.error = null
        }

        if (!isEmailValid()) {
            binding.emailInput.emailTextLayout.error = getString(R.string.invalid_email)
            isValid = false
        } else {
            binding.emailInput.emailTextLayout.error = null
        }

        if (!isPasswordValid()) {
            binding.passwordInput.passwordTextLayout.error = getString(R.string.password_must_have_8_characters)
            isValid = false
        } else {
            binding.passwordInput.passwordTextLayout.error = null
        }

        return isValid
    }

    private fun goToLogin() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupEditText() {
        binding.confirmPasswordInput.passwordTextLayout.hint = getString(R.string.confirm_password)
    }
}
