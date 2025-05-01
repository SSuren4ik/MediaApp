package com.mediaapp.playlist.presentation.playlist_screen

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mediaapp.core.models.PlaylistData
import com.mediaapp.core.utils.MusicServiceLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.playlist.R
import com.mediaapp.playlist.databinding.ActivityPlaylistBinding
import com.mediaapp.playlist.di.PlaylistDepsProvider
import com.mediaapp.playlist.domain.models.CurrentPlaylistResponseStatusModel
import com.mediaapp.playlist.presentation.playlist_screen.viewmodel.CurrentPlaylistViewModel
import com.mediaapp.playlist.presentation.playlist_screen.viewmodel.CurrentPlaylistViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrentPlaylistActivity : AppCompatActivity(), ResourceProvider {

    private lateinit var binding: ActivityPlaylistBinding
    private val viewModel: CurrentPlaylistViewModel by viewModels {
        CurrentPlaylistViewModelFactory(this)
    }
    private val playlistDiffCallback = PlaylistDiffCallback()

    @Inject
    lateinit var musicServiceLauncher: MusicServiceLauncher

    @Inject
    lateinit var playlistHostLauncher: PlaylistHostLauncher

    private val adapter by lazy {
        PlaylistRecyclerViewAdapter(
            musicServiceLauncher = musicServiceLauncher,
            addToPlaylist = { track ->
                viewModel.addTrackToPlaylist(track)
            },
            removeFromPlaylist = { track ->
                viewModel.removeTrackFromPlaylist(track)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initDI()
        setupUI()
        initRecyclerView()
        observePlaylistTracks()
        fetchPlaylistTracks()
    }

    private fun fetchPlaylistTracks() {
        val playlistId = getPlaylistId()
        if (playlistId.isNotEmpty()) {
            viewModel.getPlaylistTracks(playlistId)
        } else {
            Toast.makeText(this, "Playlist ID is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePlaylistTracks() {
        lifecycleScope.launch {
            viewModel.responseStatus.collect { result ->
                when (result) {
                    is CurrentPlaylistResponseStatusModel.Success.SuccessGetPlaylist -> {
                        displayPlaylistData(result.data)
                        adapter.setData(result.data.tracks, playlistDiffCallback)
                    }

                    is CurrentPlaylistResponseStatusModel.Error -> {
                        showToast(result.message)
                    }

                    CurrentPlaylistResponseStatusModel.Success.SuccessUpdatePlaylistName -> {
                        showToast(getString(R.string.update_playlist_name_message))
                    }

                    is CurrentPlaylistResponseStatusModel.Success.SuccessRemoveTrackFromPlaylist -> {
                        showToast(getString(R.string.remove_track_from_playlist_message))
                        fetchPlaylistTracks()
                    }

                    else -> showToast("Неизвестная ошибка")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.responseStatus.collect {
                setResult(RESULT_OK)
            }
        }
    }

    private fun getPlaylistId(): String {
        val id = intent.getStringExtra("playlistId") ?: ""
        viewModel.playlistId = id
        return id
    }

    private fun setupUI() {
        setInsets()
        with(binding.playlistName) {
            setPadding(0, 0, 0, 0)
            background = null
            isSingleLine = true
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onEditTextFocusChanged(binding.playlistName.text.toString())
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    clearFocus()
                    hideKeyboard()
                }
                false
            }
        }
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.albumImage) { view, insets ->
            val innerPadding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun displayPlaylistData(playlistData: PlaylistData) {
        val cornerRadius =
            resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.corner_radius)
        val requestOptions = RequestOptions().transform(RoundedCorners(cornerRadius))
        val imageUrl = playlistData.image.ifEmpty { com.mediaapp.music_service.R.drawable.img }

        Glide.with(this).load(imageUrl).apply(requestOptions).into(binding.albumImage)
        binding.playlistName.setText(playlistData.playlistName)
        binding.authorName.text = playlistData.authorName
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@CurrentPlaylistActivity, RecyclerView.VERTICAL, false)
            adapter = this@CurrentPlaylistActivity.adapter
            addItemDecoration(
                PlaylistItemDecorator(
                    resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.number_in_music_size)
                )
            )
        }
    }

    private fun initDI() {
        (application as PlaylistDepsProvider).getPlaylistComponent().apply {
            inject(viewModel)
            inject(this@CurrentPlaylistActivity)
        }
    }

    private fun onEditTextFocusChanged(playlistNewName: String) {
        viewModel.updatePlaylistName(playlistNewName)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.playlistName.windowToken, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
