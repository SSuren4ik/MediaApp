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
import com.mediaapp.playlist.databinding.ActivityPlaylistBinding
import com.mediaapp.playlist.di.PlaylistComponentProvider
import com.mediaapp.playlist.presentation.user_playlists.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModels()
    private val playlistDiffCallback = PlaylistDiffCallback()

    @Inject
    lateinit var musicServiceLauncher: MusicServiceLauncher

    private val adapter by lazy { PlaylistRecyclerViewAdapter(musicServiceLauncher) }

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
        val playlistName = getPlaylistName()
        if (playlistName.isNotEmpty()) {
            viewModel.getPlaylistTracks(playlistName)
        } else {
            Toast.makeText(this, "Playlist name is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePlaylistTracks() {
        lifecycleScope.launch {
            viewModel.getTracksResponseStatus.collect { playlistData ->
                displayPlaylistData(playlistData)
                adapter.setData(playlistData.tracks, playlistDiffCallback)
            }
        }
        lifecycleScope.launch {
            viewModel.updateNameResponseStatus.collect {
                setResult(RESULT_OK)
            }
        }
    }

    private fun getPlaylistName(): String {
        return intent.getStringExtra("playlistName") ?: ""
    }

    private fun setupUI() {
        setInsets()
        with(binding.playlistName) {
            setPadding(0, 0, 0, 0)
            background = null
            isSingleLine = true
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (viewModel.getPlaylistName().isNotEmpty()) onEditTextFocusChanged(
                        binding.playlistName.text.toString(), viewModel.getPlaylistName()
                    )
                    else onEditTextFocusChanged(
                        binding.playlistName.text.toString(), getPlaylistName()
                    )
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
            layoutManager = LinearLayoutManager(this@PlaylistActivity, RecyclerView.VERTICAL, false)
            adapter = this@PlaylistActivity.adapter
            addItemDecoration(
                PlaylistItemDecorator(
                    resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.number_in_music_size)
                )
            )
        }
    }

    private fun initDI() {
        (application as PlaylistComponentProvider).getPlaylistComponent().apply {
            inject(viewModel)
            inject(this@PlaylistActivity)
        }
    }

    private fun onEditTextFocusChanged(playlistNewName: String, playlistOldName: String) {
        lifecycleScope.launch {
            viewModel.updatePlaylistName(playlistNewName, playlistOldName)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.playlistName.windowToken, 0)
    }
}