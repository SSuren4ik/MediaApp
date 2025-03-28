package com.mediaapp.playlist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.design_system.databinding.PlaylistItemBinding

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.DataViewHolder>() {

    private var dataList: List<PlaylistData> = emptyList()

    fun setData(newDataList: List<PlaylistData>, playlistDiffCallback: PlaylistDiffCallback) {
        playlistDiffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(playlistDiffCallback)
        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    class DataViewHolder(val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.playlistView.setPlaylistName(data.playlistName)
        holder.binding.playlistView.setAuthorName(data.authorName)

        val cornerRadius =
            holder.itemView.context.resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.corner_radius)
        val requestOptions = RequestOptions().transform(RoundedCorners(cornerRadius))

        if (data.image.isNotEmpty()) {
            holder.binding.playlistView.setPlaylistName(data.playlistName)

            Glide.with(holder.itemView.context.applicationContext).load(data.image)
                .apply(requestOptions).into(holder.binding.playlistView.iconImage)
        } else {
            Glide.with(holder.itemView.context.applicationContext)
                .load(com.mediaapp.music_service.R.drawable.img).apply(requestOptions)
                .into(holder.binding.playlistView.iconImage)
        }
    }
}