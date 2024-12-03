package com.bangkit.spotlyze.helper.customView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val startMargin: Int, private val endMargin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == 0) {
            outRect.left = startMargin
        }

        if (position == itemCount - 1) {
            outRect.right = endMargin
        }
    }
}
