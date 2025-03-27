package com.mediaapp.music_search.presentation

import androidx.recyclerview.widget.DiffUtil
import com.mediaapp.core.models.Track

class SearchDiffCallback : DiffUtil.Callback() {

    private var oldDataList: MutableList<Track> = mutableListOf()
    private var newDataList: MutableList<Track> = mutableListOf()

    fun setData(oldItems: List<Track>, newItems: List<Track>) {
        oldDataList.clear()
        oldDataList.addAll(oldItems)

        newDataList.clear()
        newDataList.addAll(newItems)
    }

    override fun getOldListSize(): Int = oldDataList.size

    override fun getNewListSize(): Int = newDataList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition].name == newDataList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition] == newDataList[newItemPosition]
    }
}
