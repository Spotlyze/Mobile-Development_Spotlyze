package com.bangkit.spotlyze.helper

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.prayatna.spotlyze.R

object Message {
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun alertDialog(context: Context, title: String, message: String, action: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(context.getString(R.string.yes)) { _, _ -> action() }
            setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }
}