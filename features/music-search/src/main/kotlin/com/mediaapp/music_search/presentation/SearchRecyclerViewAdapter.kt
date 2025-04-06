package com.mediaapp.music_search.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.design_system.databinding.MusicItemBinding

class SearchRecyclerViewAdapter(
    private val musicServiceLauncher: MusicServiceLauncher,
    private val playlistHostLauncher: PlaylistHostLauncher,
) : RecyclerView.Adapter<SearchRecyclerViewAdapter.DataViewHolder>() {

    private var dataList: List<Track> = emptyList()

    fun setData(newDataList: List<Track>, searchDiffCallback: SearchDiffCallback) {
        searchDiffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(searchDiffCallback)
        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    class DataViewHolder(val binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        if (data.album_image.isNotEmpty()) {
            holder.binding.musicView.setText(data.name)

            val cornerRadius =
                holder.itemView.context.resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.corner_radius)
            val requestOptions = RequestOptions().transform(RoundedCorners(cornerRadius))

            Glide.with(holder.itemView.context.applicationContext).load(data.album_image)
                .apply(requestOptions).into(holder.binding.musicView.iconImage)

            holder.binding.musicView.setOnClickListener {
                musicServiceLauncher.startMusicService(holder.itemView.context, data)
            }

            holder.binding.musicView.setOnLongClickListener {
                showPopupMenu(it, data)
                true
            }
        }
    }

    private fun showPopupMenu(view: View, track: Track) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(
            com.mediaapp.design_system.R.menu.track_options_menu, popupMenu.menu
        )
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.mediaapp.design_system.R.id.add_to_playlist -> {
                    playlistHostLauncher.launchPlaylistHost(view.context, track)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}