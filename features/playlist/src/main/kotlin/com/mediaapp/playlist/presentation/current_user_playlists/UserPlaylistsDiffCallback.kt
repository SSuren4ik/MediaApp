package com.mediaapp.playlist.presentation.current_user_playlists

import androidx.recyclerview.widget.DiffUtil
import com.mediaapp.core.models.PlaylistData

class UserPlaylistsDiffCallback : DiffUtil.Callback() {

    private var oldDataList: MutableList<PlaylistData> = mutableListOf()
    private var newDataList: MutableList<PlaylistData> = mutableListOf()

    fun setData(oldItems: List<PlaylistData>, newItems: List<PlaylistData>) {
        oldDataList.clear()
        oldDataList.addAll(oldItems)

        newDataList.clear()
        newDataList.addAll(newItems)
    }

    override fun getOldListSize(): Int {
        return oldDataList.size
    }

    override fun getNewListSize(): Int {
        return newDataList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition].playlistName == newDataList[newItemPosition].playlistName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition] == newDataList[newItemPosition]
    }
}