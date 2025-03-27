package com.mediaapp.playlist.presentation.user_playlists

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.databinding.FragmentPlaylistBinding
import com.mediaapp.playlist.di.PlaylistDepsProvider
import com.mediaapp.playlist.domain.models.UserPlaylistsResponseStatusModel
import com.mediaapp.playlist.presentation.playlist_screen.CurrentPlaylistActivity
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.UserPlaylistsViewModel
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.UserPlaylistsViewModelFactory
import kotlinx.coroutines.launch

class UserPlaylistsFragment : Fragment(), ResourceProvider {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: UserPlaylistsViewModel by viewModels {
        UserPlaylistsViewModelFactory(this)
    }
    private var playlistDiffCallback = UserPlaylistsDiffCallback()
    private var mode: Boolean = false
    private var track: Track? = null

    private val playlistActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getUserPlaylists()
            }
        }

    private val adapter: PlaylistAdapter by lazy {
        PlaylistAdapter { playlistName ->
            if (mode) {
                track?.let {
                    viewModel.addSongToPlaylist(playlistName, it)
                }
                requireActivity().finish()
            } else {
                val intent = Intent(requireContext(), CurrentPlaylistActivity::class.java).apply {
                    putExtra("playlistName", playlistName)
                }
                playlistActivityLauncher.launch(intent)
            }
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
        initFragmentMode()

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
                when (result) {
                    is UserPlaylistsResponseStatusModel.Success.SuccessGetUserPlaylists -> {
                        adapter.setData(result.data, playlistDiffCallback)
                    }

                    is UserPlaylistsResponseStatusModel.Error -> {
                        showToast(result.message)
                    }

                    is UserPlaylistsResponseStatusModel.Success.SuccessCreatePlaylist -> {
                        viewModel.getUserPlaylists()
                    }

                    UserPlaylistsResponseStatusModel.Success.SuccessAddSongToPlaylist -> {
                        showToast(getString(R.string.add_track_message))
                    }
                }
            }
        }
    }

    private fun initDI() {
        val playlistComponent =
            (requireActivity().application as PlaylistDepsProvider).getPlaylistComponent()
        playlistComponent.inject(viewModel)
    }

    private fun initFragmentMode() {
        arguments?.let {
            mode = it.getBoolean("mode", false)
            track = if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) it.getParcelable(
                "track", Track::class.java
            )
            else it.getParcelable("track")
        }

        binding.addPlaylistButton.isVisible = !mode
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(mode: Boolean, track: Track) = UserPlaylistsFragment().apply {
            arguments = Bundle().apply {
                putBoolean("mode", mode)
                putParcelable("track", track)
            }
        }
    }
}