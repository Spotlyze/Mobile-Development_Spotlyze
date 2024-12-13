package com.bangkit.spotlyze.helper.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.prayatna.spotlyze.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setupValidation()
        hint = context.getString(R.string.password)
        inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun setupValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = this@PasswordEditText.parent.parent
                if (parent is TextInputLayout) {
                    if (s.toString().length < 8) {
                        parent.error = context.getString(R.string.password_must_have_8_characters)
                        parent.errorIconDrawable = null
                    } else {
                        parent.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
