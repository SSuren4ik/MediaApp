package com.mediaapp.playlist.presentation.current_user_playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.design_system.databinding.PlaylistItemBinding

class UserPlaylistsAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<UserPlaylistsAdapter.DataViewHolder>() {

    private var dataList: List<PlaylistData> = emptyList()

    fun setData(newDataList: List<PlaylistData>, playlistDiffCallback: UserPlaylistsDiffCallback) {
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
        with(holder.binding.playlistView) {
            setPlaylistName(data.playlistName)
            setAuthorName(data.authorName)
            setOnClickListener { onItemClick(data.playlistId) }

            val context = holder.itemView.context
            val cornerRadius =
                context.resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.corner_radius)
            val requestOptions = RequestOptions().transform(RoundedCorners(cornerRadius))

            Glide.with(context.applicationContext)
                .load(data.image.ifEmpty { com.mediaapp.music_service.R.drawable.img })
                .apply(requestOptions).into(holder.binding.playlistView.iconImage)
        }
    }
}