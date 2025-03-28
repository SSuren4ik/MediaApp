package com.mediaapp.user_search.presentation.search_screen

import androidx.recyclerview.widget.DiffUtil
import com.mediaapp.user_search.domain.UserData

class UserSearchDiffCallback : DiffUtil.Callback() {

    private var oldDataList: MutableList<UserData> = mutableListOf()
    private var newDataList: MutableList<UserData> = mutableListOf()

    fun setData(oldItems: List<UserData>, newItems: List<UserData>) {
        oldDataList.clear()
        oldDataList.addAll(oldItems)

        newDataList.clear()
        newDataList.addAll(newItems)
    }

    override fun getOldListSize(): Int = oldDataList.size

    override fun getNewListSize(): Int = newDataList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition].name.length == newDataList[newItemPosition].name.length
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition] == newDataList[newItemPosition]
    }
}
