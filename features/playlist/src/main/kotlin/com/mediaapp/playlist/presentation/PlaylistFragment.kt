package com.mediaapp.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.models.Track
import com.mediaapp.playlist.databinding.FragmentPlaylistBinding
import com.mediaapp.playlist.di.PlaylistComponentProvider
import com.mediaapp.playlist.presentation.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val adapter: PlaylistAdapter by lazy { PlaylistAdapter() }

    private val viewModel: PlaylistViewModel by viewModels()

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
        initRecyclerViews()
        addShimmers()
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistTitle) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                0, innerPadding.top, 0, 0
            )
            insets
        }
    }

    private fun addShimmers() {
        adapter.setData(
            listOf(PlaylistData(), PlaylistData(), PlaylistData()), PlaylistDiffCallback()
        )
    }

    private fun initDI() {
        val playlistComponent =
            (requireActivity().application as PlaylistComponentProvider).getPlaylistComponent()
        playlistComponent.inject(viewModel)
        playlistComponent.inject(this)
    }

    private fun initRecyclerViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }
}