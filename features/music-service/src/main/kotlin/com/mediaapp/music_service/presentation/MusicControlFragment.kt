package com.mediaapp.music_service.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediaapp.core.models.MusicDataForService
import com.mediaapp.music_service.R
import com.mediaapp.music_service.databinding.FragmentMusicControlBinding

class MusicControlFragment : Fragment() {

    private lateinit var binding: FragmentMusicControlBinding
    private lateinit var currentTrack: MusicDataForService
    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    MusicService.Actions.TRACK_CHANGED.name -> {
                        setCurrentTrack(it)
                    }

                    MusicService.Actions.PLAYING_STATE_CHANGED.name -> {
                        val isPlaying = it.getBooleanExtra("isPlaying", false)
                        updatePlayPauseButton(isPlaying)
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMusicControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        registerReceiver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
    }

    private fun setupUI() {
        binding.musicControlPlayPause.setOnClickListener {
            val intent = Intent(context, MusicService::class.java).apply {
                action = if (MusicService.player.isPlaying) {
                    MusicService.Actions.PAUSE.name
                } else {
                    MusicService.Actions.PLAY.name
                }
            }
            context?.startService(intent)
        }
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) {
            binding.musicControlPlayPause.setImageResource(R.drawable.baseline_pause_24)
        } else {
            binding.musicControlPlayPause.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter().apply {
            addAction(MusicService.Actions.TRACK_CHANGED.name)
            addAction(MusicService.Actions.PLAYING_STATE_CHANGED.name)
        }
        requireContext().registerReceiver(musicReceiver, filter)
    }

    private fun unregisterReceiver() {
        requireContext().unregisterReceiver(musicReceiver)
    }

    fun setCurrentTrack(intent: Intent) {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                "track", MusicDataForService::class.java
            )
        } else {
            intent.getParcelableExtra("track")
        }
        if (track != null) {
            this.currentTrack = track
            binding.musicControlTitle.text = currentTrack.musicName
            binding.musicControlIcon.setImageBitmap(currentTrack.image)
        }
    }

    companion object {
        fun newInstance(): MusicControlFragment {
            return MusicControlFragment()
        }
    }
}