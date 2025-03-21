package com.mediaapp.music_service.presentation

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.mediaapp.music_service.R

class MusicService : Service() {

    private lateinit var player: ExoPlayer
    private lateinit var currentUri: String
    private var isSameUrl: Boolean = false

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val newUri = it.getStringExtra("URI").toString()
            if (::currentUri.isInitialized) {
                isSameUrl = newUri == currentUri
            }
            currentUri = newUri
            when (it.action) {
                Actions.STOP.name -> stopSelf()
                Actions.PLAY.name -> play()
                Actions.PAUSE.name -> pause()
            }
        }
        return START_STICKY
    }

    private fun play() {
        player.let {
            if (!isSameUrl) {
                it.stop()
                it.setMediaItem(MediaItem.fromUri(currentUri))
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

    private fun createNotification(isPlaying: Boolean = false): Notification {
        val playIntent = Intent(this, MusicService::class.java).apply { action = Actions.PLAY.name }
        val playPendingIntent =
            PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent =
            Intent(this, MusicService::class.java).apply { action = Actions.PAUSE.name }
        val pausePendingIntent =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val stopIntent = Intent(this, MusicService::class.java).apply { action = Actions.STOP.name }
        val stopPendingIntent =
            PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(this, "running_channel")
            .setContentTitle("MyService")
            .setContentText(if (isPlaying) "Playing Music" else "Music Paused")
            .setSmallIcon(R.drawable.img)
            .addAction(R.drawable.baseline_play_arrow_24, "Play", playPendingIntent)
            .addAction(R.drawable.baseline_pause_24, "Pause", pausePendingIntent)
            .addAction(R.drawable.baseline_stop_circle_24, "Stop", stopPendingIntent)
            .build()
    }

    private fun updateNotification(isPlaying: Boolean) {
        val notification = createNotification(isPlaying)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    enum class Actions {
        STOP, PLAY, PAUSE
    }
}
