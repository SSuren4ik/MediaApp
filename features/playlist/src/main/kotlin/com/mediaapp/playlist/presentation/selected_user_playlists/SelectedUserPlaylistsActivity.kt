package com.mediaapp.playlist.presentation.selected_user_playlists

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediaapp.core.utils.PlaylistLauncher
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.databinding.ActivitySelectedUserPlaylistsBinding
import com.mediaapp.playlist.di.PlaylistDepsProvider
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.presentation.selected_user_playlists.viewmodel.SelectedUserPlaylistsViewModel
import com.mediaapp.playlist.presentation.selected_user_playlists.viewmodel.SelectedUserPlaylistsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedUserPlaylistsActivity : AppCompatActivity(), ResourceProvider {

    private lateinit var binding: ActivitySelectedUserPlaylistsBinding
    private val viewModel: SelectedUserPlaylistsViewModel by viewModels {
        SelectedUserPlaylistsViewModelFactory(this)
    }

    private val playlistDiffCallback = UserPlaylistsDiffCallback()

    @Inject
    lateinit var playlistLauncher: PlaylistLauncher

    private lateinit var userId: String
    private lateinit var username: String

    private val adapter: SelectedUserPlaylistsAdapter by lazy {
        SelectedUserPlaylistsAdapter(
            onItemClick = { playlistData ->
                viewModel.saveSelectedPlaylist(playlistData)
            }, playlistLauncher = playlistLauncher
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedUserPlaylistsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        getUserName()
        initDI()
        initUI()
        observeUserPlaylists()
        viewModel.getUserPlaylists(userId)
    }

    private fun initUI() {
        setInsets()
        initRecyclerView()
        binding.playlistTitle.text = "Плейлисты ${username}"
    }

    private fun getUserName() {
        userId = intent.getStringExtra("userId") ?: ""
        username = intent.getStringExtra("username") ?: ""
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistTitle) { view, insets ->
            val innerPadding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun observeUserPlaylists() {
        lifecycleScope.launch {
            viewModel.responseStatus.collect { result ->
                when (result) {
                    is UserPlaylistsResponseStatusModel.Success.SuccessGetUserPlaylists -> {
                        adapter.setData(result.data, playlistDiffCallback)
                    }

                    is UserPlaylistsResponseStatusModel.Error -> {
                        showToast(result.message)
                    }

                    is UserPlaylistsResponseStatusModel.Success.SuccessAddSongToPlaylist -> {
                        showToast(getString(R.string.add_track_message))
                    }

                    is UserPlaylistsResponseStatusModel.Success.SuccessSaveSelectedPlaylist -> {
                        showToast(getString(R.string.save_playlist_message))
                        finish()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initDI() {
        val component = (application as PlaylistDepsProvider).getPlaylistComponent()
        component.inject(viewModel)
        component.inject(this)
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SelectedUserPlaylistsActivity)
            adapter = this@SelectedUserPlaylistsActivity.adapter
            addItemDecoration(
                ItemSpacingDecorator(
                    resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.playlist_top_margin)
                )
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}