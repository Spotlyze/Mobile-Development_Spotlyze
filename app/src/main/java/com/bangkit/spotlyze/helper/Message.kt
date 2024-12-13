package com.bangkit.spotlyze.helper

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prayatna.spotlyze.R

object Message {
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun offlineDialog(context: Context, action: () -> Unit) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.isDraggable = false
        bottomSheetDialog.behavior.peekHeight = context.resources.displayMetrics.heightPixels / 2
        view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_try_again).setOnClickListener {
            action()
        }
        view.findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.btn_close).setOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }
    }

    fun bottomSheetDialog(context: Context, title: String, description: String) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.skincare_info_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        val closeButton = view.findViewById<ImageView>(R.id.ivClose)
        closeButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.behavior.peekHeight = context.resources.displayMetrics.heightPixels / 2
        view.findViewById<TextView>(R.id.tvTitle).text = title
        view.findViewById<TextView>(R.id.tvDescription).text = description
    }
}