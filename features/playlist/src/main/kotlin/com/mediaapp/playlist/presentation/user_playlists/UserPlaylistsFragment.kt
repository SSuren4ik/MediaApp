package com.mediaapp.playlist.presentation.user_playlists

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediaapp.playlist.databinding.FragmentPlaylistBinding
import com.mediaapp.playlist.di.PlaylistComponentProvider
import com.mediaapp.playlist.presentation.playlist_screen.PlaylistActivity
import com.mediaapp.playlist.presentation.playlist_screen.PlaylistItemDecorator
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.UserPlaylistsViewModel
import kotlinx.coroutines.launch

class UserPlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: UserPlaylistsViewModel by viewModels()
    private var playlistDiffCallback = UserPlaylistsDiffCallback()

    private val playlistActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getUserPlaylists()
            }
        }

    private val adapter: PlaylistAdapter by lazy {
        PlaylistAdapter { playlistName ->
            val intent = Intent(requireContext(), PlaylistActivity::class.java).apply {
                putExtra("playlistName", playlistName)
            }
            playlistActivityLauncher.launch(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        setInsets()
        initRecyclerView()
        observeUserPlaylists()
        viewModel.getUserPlaylists()

        binding.addPlaylistButton.setOnClickListener {
            viewModel.createPlaylist("New Playlist")
        }
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
                adapter.setData(result, playlistDiffCallback)
            }
        }
        lifecycleScope.launch {
            viewModel.createStatus.collect {
                viewModel.getUserPlaylists()
            }
        }
    }

    private fun initDI() {
        val playlistComponent =
            (requireActivity().application as PlaylistComponentProvider).getPlaylistComponent()
        playlistComponent.inject(viewModel)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            ItemSpacingDecorator(
                resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.playlist_top_margin)
            )
        )
    }
}