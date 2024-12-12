package com.bangkit.spotlyze.helper.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.prayatna.spotlyze.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setupInputValidation()
        setupAttributes()
        setupFocusListener()
    }

    private fun setupAttributes() {
        hint = context.getString(R.string.email)
        inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or android.text.InputType.TYPE_CLASS_TEXT
    }

    private fun setupInputValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validateEmail(email: CharSequence?) {
        val parent = this.parent?.parent
        if (parent is TextInputLayout) {
            when {
                email.isNullOrEmpty() -> {
                    parent.error = context.getString(R.string.email_required)
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    parent.error = context.getString(R.string.invalid_email)
                }
                else -> {
                    parent.error = null
                }
            }
        }
    }

    private fun setupFocusListener() {
        this.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val parent = this@EmailEditText.parent.parent
                if (parent is TextInputLayout) {
                    parent.error = null
                }
            }
        }
    }
}
