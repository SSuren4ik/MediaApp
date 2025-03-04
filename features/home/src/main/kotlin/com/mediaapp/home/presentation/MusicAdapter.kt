package com.mediaapp.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mediaapp.core.data.Track
import com.mediaapp.design_system.databinding.AlbumItemBinding

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.DataViewHolder>() {

    private var dataList: List<Track> = emptyList()

    fun setData(newDataList: List<Track>, diffCallback: DiffCallback) {
        diffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    class DataViewHolder(val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        if (data.album_image.isEmpty()) {
            holder.binding.albumView.iconImage.setImageResource(com.mediaapp.design_system.R.drawable.music_standard_icon)
        } else {
            holder.binding.albumView.setAlbumName(data.name)
            holder.binding.albumView.setArtistName(data.artist_name)
            Glide.with(holder.itemView.context.applicationContext).load(data.album_image)
                .placeholder(com.mediaapp.design_system.R.drawable.music_standard_icon)
                .into(holder.binding.albumView.iconImage)
        }
    }
}