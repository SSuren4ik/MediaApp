package com.mediaapp.music_search.presentation

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
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.music_search.databinding.FragmentSearchBinding
import com.mediaapp.music_search.di.SearchDepsProvider
import com.mediaapp.music_search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val diffCallback = SearchDiffCallback()
    private val adapter: SearchRecyclerViewAdapter by lazy {
        SearchRecyclerViewAdapter(musicServiceLauncher, playlistHostLauncher)
    }

    @Inject
    lateinit var musicServiceLauncher: MusicServiceLauncher

    @Inject
    lateinit var playlistHostLauncher: PlaylistHostLauncher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setInsets()
        initUI()
        initDI()
        initRecyclerView()
        observeMusic()
        viewModel.getMusicByPrefix(binding.searchEditText.text.toString())
    }

    private fun observeMusic() {
        lifecycleScope.launch {
            viewModel.responseStatus.collect { result ->
                adapter.setData(result, diffCallback)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun initUI() {
        binding.searchEditText.apply {
            isSingleLine = true
            addTextChangedListener(DelayedTextWatcher { prefix ->
                viewModel.getMusicByPrefix(prefix)
            })
        }
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchLinear) { view, insets ->
            val innerPadding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun initDI() {
        val searchComponent =
            (requireActivity().application as SearchDepsProvider).getSearchComponent()
        searchComponent.inject(viewModel)
        searchComponent.inject(this)
    }
}