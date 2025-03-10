package com.mediaapp.album.presentation

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecorator(
    private val lineColor: Int,
    private val lineWidth: Float,
    private val numberWidthFixed: Int,
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = lineColor
        strokeWidth = lineWidth
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
