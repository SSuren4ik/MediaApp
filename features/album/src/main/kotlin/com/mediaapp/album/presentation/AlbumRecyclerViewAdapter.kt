package com.mediaapp.album.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.design_system.databinding.MusicInAlbumItemBinding

class AlbumRecyclerViewAdapter(
    private val musicServiceLauncher: MusicServiceLauncher,
    private val playlistHostLauncher: PlaylistHostLauncher,
) : RecyclerView.Adapter<AlbumRecyclerViewAdapter.DataViewHolder>() {

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
            musicServiceLauncher.startMusicService(holder.itemView.context, data)
        }

        holder.binding.musicView.setOnLongClickListener {
            showPopupMenu(it, data)
            true
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