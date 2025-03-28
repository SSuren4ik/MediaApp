package com.mediaapp.user_search.presentation.search_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.user_search.databinding.UserItemBinding
import com.mediaapp.user_search.domain.UserData

class UserSearchRecyclerViewAdapter(
    private val selectedUserPlaylistsLauncher: SelectedUserPlaylistsLauncher,
) : RecyclerView.Adapter<UserSearchRecyclerViewAdapter.DataViewHolder>() {

    private var dataList: List<UserData> = emptyList()

    fun setData(newDataList: List<UserData>, userSearchDiffCallback: UserSearchDiffCallback) {
        userSearchDiffCallback.setData(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(userSearchDiffCallback)
        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    class DataViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.userNameTextview.text = data.name
        holder.binding.userNameTextview.setOnClickListener {
            selectedUserPlaylistsLauncher.selectedUserPlaylistsLaunch(
                holder.itemView.context, data.id, data.name
            )
        }
    }
}
