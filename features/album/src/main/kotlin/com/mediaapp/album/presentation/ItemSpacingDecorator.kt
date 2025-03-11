package com.mediaapp.album.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecorator(
    private val numberWidthFixed: Int,
) : RecyclerView.ItemDecoration() {

    private val lineWidth = 1f

    private val paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = lineWidth
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position == state.itemCount - 1) {
            outRect.bottom = lineWidth.toInt()
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left
            val right = child.right
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin

            if (i == 0) {
                c.drawLine(
                    left.toFloat(), top + lineWidth / 2, right.toFloat(), top + lineWidth / 2, paint
                )
            }

            if (i == childCount - 1) {
                c.drawLine(
                    left.toFloat(),
                    bottom + lineWidth / 2,
                    right.toFloat(),
                    bottom + lineWidth / 2,
                    paint
                )
            } else {
                c.drawLine(
                    (left + numberWidthFixed).toFloat(),
                    bottom + lineWidth / 2,
                    right.toFloat(),
                    bottom + lineWidth / 2,
                    paint
                )
            }
        }
    }
}
