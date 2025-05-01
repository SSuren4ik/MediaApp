package com.mediaapp.music_service.presentation

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mediaapp.core.models.MusicDataForService
import com.mediaapp.music_service.R

class MusicService : Service() {

    companion object {
        lateinit var player: ExoPlayer
    }

    private lateinit var currentTrack: MusicDataForService
    private var isSameUrl: Boolean = false

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Actions.STOP.name -> stopSelf()
                Actions.PLAY.name -> {
                    val index = it.getIntExtra("track_index", -1)
                    if (index != -1) MusicPlaylistHolder.currentIndex = index
                    MusicPlaylistHolder.getCurrentTrack()?.let { track ->
                        currentTrack = track
                        isSameUrl = false
                        play()
                    }
                }
                Actions.PAUSE.name -> pause()
                Actions.NEXT.name -> {
                    MusicPlaylistHolder.getNextTrack()?.let { track ->
                        currentTrack = track
                        isSameUrl = false
                        play()
                    }
                }
                Actions.PREVIOUS.name -> {
                    MusicPlaylistHolder.getPreviousTrack()?.let { track ->
                        currentTrack = track
                        isSameUrl = false
                        play()
                    }
                }

                else -> {}
            }
        }
        return START_STICKY
    }

    private fun play() {
        player.let {
            if (!isSameUrl) {
                it.stop()
                it.setMediaItem(MediaItem.fromUri(currentTrack.audio))
                it.prepare()
                it.play()
            } else if (!it.isPlaying) {
                it.play()
            }
            updateNotification(true)
        }
    }

    private fun pause() {
        player.pause()
        updateNotification(false)
    }

    private fun getCustomRemoteViews(isPlaying: Boolean): RemoteViews {
        val remoteViews = RemoteViews(packageName, R.layout.notification_layout)

        remoteViews.setTextViewText(R.id.music_name, currentTrack.musicName)
        remoteViews.setTextViewText(R.id.artist_name, currentTrack.artistName)

        val bitmap = currentTrack.image
        remoteViews.setImageViewBitmap(R.id.notification_icon, bitmap)

        val playPauseDrawable = if (isPlaying) {
            R.drawable.baseline_pause_24
        } else {
            R.drawable.baseline_play_arrow_24
        }
        remoteViews.setImageViewResource(R.id.notification_play_pause, playPauseDrawable)

        val playPauseIntent = Intent(this, MusicService::class.java).apply {
            action = if (isPlaying) Actions.PAUSE.name else Actions.PLAY.name
        }
        val playPausePendingIntent = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause, playPausePendingIntent)

        val previousIntent = Intent(this, MusicService::class.java).apply {
            action = Actions.PREVIOUS.name
        }
        val previousPendingIntent = PendingIntent.getService(this, 1, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        remoteViews.setOnClickPendingIntent(R.id.notification_previous, previousPendingIntent)

        val nextIntent = Intent(this, MusicService::class.java).apply {
            action = Actions.NEXT.name
        }
        val nextPendingIntent = PendingIntent.getService(this, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        remoteViews.setOnClickPendingIntent(R.id.notification_next, nextPendingIntent)

        return remoteViews
    }

    private fun createNotification(isPlaying: Boolean): Notification {
        return NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.img)
            .setCustomContentView(getCustomRemoteViews(isPlaying))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomHeadsUpContentView(getCustomRemoteViews(isPlaying))
            .setCustomBigContentView(getCustomRemoteViews(isPlaying)).setOngoing(true).build()
    }

    private fun updateNotification(isPlaying: Boolean) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, createNotification(isPlaying))
    }

    override fun onDestroy() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    enum class Actions {
        STOP, PLAY, PAUSE, NEXT, PREVIOUS
    }

    object MusicPlaylistHolder {
        var trackList: List<MusicDataForService> = emptyList()
        var currentIndex: Int = 0

        fun getCurrentTrack(): MusicDataForService? =
            trackList.getOrNull(currentIndex)

        fun getNextTrack(): MusicDataForService? {
            if (trackList.isEmpty()) return null
            currentIndex = (currentIndex + 1) % trackList.size
            return trackList[currentIndex]
        }

        fun getPreviousTrack(): MusicDataForService? {
            if (trackList.isEmpty()) return null
            currentIndex = if (currentIndex - 1 < 0) trackList.size - 1 else currentIndex - 1
            return trackList[currentIndex]
        }

    }
}