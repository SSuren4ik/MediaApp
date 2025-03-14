package com.mediaapp.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.LauncherAlbum
import com.mediaapp.design_system.databinding.AlbumItemBinding

class MusicAdapter(private val launcherAlbum: LauncherAlbum) :
    RecyclerView.Adapter<MusicAdapter.DataViewHolder>() {

    private var dataList: List<Track> = emptyList()

    fun setData(newDataList: List<Track>, homeDiffCallback: HomeDiffCallback) {
        homeDiffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(homeDiffCallback)
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
        if (data.album_image.isNotEmpty()) {
            holder.binding.albumView.setAlbumName(data.name)
            holder.binding.albumView.setArtistName(data.artist_name)

            val cornerRadius =
                holder.itemView.context.resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.corner_radius)
            val requestOptions = RequestOptions().transform(RoundedCorners(cornerRadius))

            Glide.with(holder.itemView.context.applicationContext).load(data.album_image)
                .apply(requestOptions).into(holder.binding.albumView.iconImage)

            holder.binding.albumView.setOnClickListener {
                launcherAlbum.launchAlbum(holder.itemView.context, data)
            }
        }
    }
}