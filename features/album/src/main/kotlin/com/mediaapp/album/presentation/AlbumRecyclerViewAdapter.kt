package com.mediaapp.album.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.design_system.databinding.MusicInAlbumItemBinding

class AlbumRecyclerViewAdapter(private val musicServiceLauncher: MusicServiceLauncher) :
    RecyclerView.Adapter<AlbumRecyclerViewAdapter.DataViewHolder>() {

    private var dataList: List<Track> = emptyList()

    fun setData(newDataList: List<Track>, albumDiffCallback: AlbumDiffCallback) {
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
        holder.binding.musicView.setOnLongClickListener {
            TODO()
            true
        }
    }
}