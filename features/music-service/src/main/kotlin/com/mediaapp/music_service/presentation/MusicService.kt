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
                sendPlayingStateChangedBroadcast(isPlaying)
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val newTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableExtra("track", MusicDataForService::class.java)
            } else {
                it.getParcelableExtra("track")
            }

            if (newTrack != null) {
                if (::currentTrack.isInitialized) {
                    isSameUrl = newTrack.audio == currentTrack.audio
                }
                currentTrack = newTrack
                sendTrackChangedBroadcast(newTrack)
            } else {
                isSameUrl = true
            }

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

        return remoteViews
    }

    private fun createNotification(isPlaying: Boolean): Notification {
        return NotificationCompat.Builder(this, "running_channel").setSmallIcon(R.drawable.img)
            .setCustomContentView(getCustomRemoteViews(isPlaying))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomHeadsUpContentView(getCustomRemoteViews(isPlaying))
            .setCustomBigContentView(getCustomRemoteViews(isPlaying)).setOngoing(true).build()
    }

    private fun updateNotification(isPlaying: Boolean) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, createNotification(isPlaying))
    }

    private fun sendTrackChangedBroadcast(track: MusicDataForService) {
        val intent = Intent(Actions.TRACK_CHANGED.name).apply {
            putExtra("track", track)
        }
        sendBroadcast(intent)
    }

    private fun sendPlayingStateChangedBroadcast(isPlaying: Boolean) {
        val intent = Intent(Actions.PLAYING_STATE_CHANGED.name).apply {
            putExtra("isPlaying", isPlaying)
        }
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    enum class Actions {
        STOP, PLAY, PAUSE, TRACK_CHANGED, PLAYING_STATE_CHANGED
    }
}