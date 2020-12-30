package com.andyprojects.books.shelf

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(private val divider: Drawable)
    : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        parent.adapter?.let {adapter ->
            parent.children.forEach { view ->
                val position = parent.getChildAdapterPosition(view)
                    .let {
                        if(it == RecyclerView.NO_POSITION) return
                        else it
                    }
                if(position != adapter.itemCount - 1) {
                    val left = view.left
                    val top = view.bottom
                    val right = view.width + left
                    val bottom = divider.intrinsicHeight + top
                    divider.bounds = Rect(left, top, right, bottom)
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        parent.adapter?.let { adapter ->
            parent.children.forEach { view ->
                val position = parent.getChildAdapterPosition(view)
                    .let {
                        if(it == RecyclerView.NO_POSITION) return
                        else it
                    }
                outRect.bottom = if(position == adapter.itemCount - 1) 0
                else divider.intrinsicWidth
            }
        }
    }

}