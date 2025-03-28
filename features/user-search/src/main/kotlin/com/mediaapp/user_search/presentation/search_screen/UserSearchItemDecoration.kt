package com.mediaapp.user_search.presentation.search_screen

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class UserSearchItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top = spacing
    }
}