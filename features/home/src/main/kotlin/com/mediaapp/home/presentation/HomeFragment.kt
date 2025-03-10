package com.mediaapp.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.LauncherAlbum
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.home.R
import com.mediaapp.home.databinding.FragmentHomeBinding
import com.mediaapp.home.di.HomeDepsProvider
import com.mediaapp.home.domain.models.ResponseStatus
import com.mediaapp.home.presentation.viewmodel.HomeViewModel
import com.mediaapp.home.presentation.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var router: LauncherAlbum

    private lateinit var binding: FragmentHomeBinding
    private val popularMusicAdapter: MusicAdapter by lazy { MusicAdapter(router) }
    private val newMusicAdapter: MusicAdapter by lazy { MusicAdapter(router) }
    private val topDownloadsMusicAdapter: MusicAdapter by lazy { MusicAdapter(router) }
    private var homeDiffCallback = HomeDiffCallback()

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireActivity().application as ResourceProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        initRecyclerViews()
        setPadding()
        addShimmers()
        observeMusicState()
        getMusic()
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.popularTitleTextView) { view, insets ->
            val innerPadding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun getMusic() {
        lifecycleScope.launch {
            viewModel.getMusic()
        }
    }

    private fun addShimmers() {
        listOf(popularMusicAdapter, newMusicAdapter, topDownloadsMusicAdapter).forEach { adapter ->
            adapter.setData(listOf(Track(), Track(), Track()), homeDiffCallback)
        }
    }

    private fun observeMusicState() {
        lifecycleScope.launch {
            viewModel.responseStatus.collect { result ->
                when (result) {
                    is ResponseStatus.Error -> showToast(result.error)
                    is ResponseStatus.SuccessResponse -> {
                        updateMusicData(result)
                    }
                }
            }
        }
    }

    private fun initRecyclerViews() {
        initRecyclerView(binding.popularMusicRecyclerView, popularMusicAdapter)
        initRecyclerView(binding.newMusicRecyclerView, newMusicAdapter)
        initRecyclerView(binding.topDownloadsMusicRecyclerView, topDownloadsMusicAdapter)
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: MusicAdapter) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        recyclerView.adapter = adapter

        val spacingInPixels =
            resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.item_spacing)
        recyclerView.addItemDecoration(ItemSpacingDecorator(spacingInPixels))
    }

    private fun updateMusicData(data: ResponseStatus.SuccessResponse) {
        newMusicAdapter.setData(data.newMusic.value, homeDiffCallback)
        popularMusicAdapter.setData(data.popularMusic.value, homeDiffCallback)
        topDownloadsMusicAdapter.setData(data.topDownloadsMusic.value, homeDiffCallback)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun initDI() {
        val homeComponent =
            (requireContext().applicationContext as HomeDepsProvider).getHomeComponent()
        homeComponent.inject(viewModel)
        homeComponent.inject(this)
    }
}