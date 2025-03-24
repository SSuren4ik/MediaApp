package com.mediaapp.playlist.presentation.playlist_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.design_system.databinding.MusicInAlbumItemBinding

class PlaylistRecyclerViewAdapter(
    private val musicServiceLauncher: MusicServiceLauncher,
) : RecyclerView.Adapter<PlaylistRecyclerViewAdapter.DataViewHolder>() {

    private var dataList: List<Track> = emptyList()

    fun setData(newDataList: List<Track>, albumDiffCallback: PlaylistDiffCallback) {
        albumDiffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(albumDiffCallback)
        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    class DataViewHolder(val binding: MusicInAlbumItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            MusicInAlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.musicView.setMusicName(data.name)
        holder.binding.musicView.setMusicNumber((position + 1).toString())
        holder.binding.musicView.setOnClickListener {
            musicServiceLauncher.startMusicService(holder.itemView.context, data.audio)
        }
    }
}