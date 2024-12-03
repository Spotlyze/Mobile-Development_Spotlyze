package com.bangkit.spotlyze.helper.customView

import android.content.Context
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

class BoundEdgeEffect(private val context: Context) : RecyclerView.EdgeEffectFactory(){
    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(context) {
            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance / 3, displacement)
                view.invalidate()
            }

            override fun onRelease() {
                super.onRelease()
                view.invalidate()
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity / 2)
                view.invalidate()
            }
        }
    }

}