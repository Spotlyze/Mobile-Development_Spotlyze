package com.bangkit.spotlyze.helper

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    fun offlineDialog(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        bottomSheetDialog.setOnDismissListener {
            finishAffinity(context as Activity)
        }

    }
}