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
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.mediaapp.core.data.Track
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.home.databinding.FragmentHomeBinding
import com.mediaapp.home.di.HomeDepsProvider
import com.mediaapp.home.domain.models.ResponseStatus
import com.mediaapp.home.presentation.viewmodel.HomeViewModel
import com.mediaapp.home.presentation.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var popularMusicAdapter: MusicAdapter
    private lateinit var newMusicAdapter: MusicAdapter
    private lateinit var topDownloadsMusicAdapter: MusicAdapter
    private var diffCallback = DiffCallback()

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireActivity().application as ResourceProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        initRecyclerViews()
        setPadding()
        addShimmers()

        lifecycleScope.launch {
            observePopularMusicState()
        }

        lifecycleScope.launch {
            viewModel.getMusic()
        }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.popularTittleTextView) { view, insets ->
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
        popularMusicAdapter.setData(listOf(Track(), Track(), Track()), diffCallback)
        newMusicAdapter.setData(listOf(Track(), Track(), Track()), diffCallback)
        topDownloadsMusicAdapter.setData(listOf(Track(), Track(), Track()), diffCallback)
    }

    private suspend fun observePopularMusicState() {
        viewModel.responseStatus.collect { result ->
            when (result) {
                is ResponseStatus.Error -> showToast(result.error)

                is ResponseStatus.SuccessResponse -> {
                    updateMusicData(result)
                }
            }
        }
    }

    private fun initRecyclerViews() {
        initPopularRecyclerView()
        initNewReleasesRecyclerView()
        initTopDownloadsRecyclerView()
    }

    private fun initPopularRecyclerView() {
        popularMusicAdapter = MusicAdapter()
        binding.popularMusicRecyclerView.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                orientation = HORIZONTAL
            }
        binding.popularMusicRecyclerView.adapter = popularMusicAdapter
    }

    private fun initNewReleasesRecyclerView() {
        newMusicAdapter = MusicAdapter()
        binding.newMusicRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = HORIZONTAL
        }
        binding.newMusicRecyclerView.adapter = newMusicAdapter
    }

    private fun initTopDownloadsRecyclerView() {
        topDownloadsMusicAdapter = MusicAdapter()
        binding.topDownloadsMusicRecyclerView.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                orientation = HORIZONTAL
            }
        binding.topDownloadsMusicRecyclerView.adapter = topDownloadsMusicAdapter
    }

    private fun updateMusicData(data: ResponseStatus.SuccessResponse) {
        newMusicAdapter.setData(data.newMusic.value, diffCallback)
        popularMusicAdapter.setData(data.popularMusic.value, diffCallback)
        topDownloadsMusicAdapter.setData(data.topDownloadsMusic.value, diffCallback)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun initDI() {
        val weatherComponent =
            (requireContext().applicationContext as HomeDepsProvider).getHomeComponent()
        weatherComponent.inject(viewModel)
    }
}