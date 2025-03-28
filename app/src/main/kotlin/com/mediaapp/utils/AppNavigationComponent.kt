package com.mediaapp.utils

import android.content.Context
import android.content.Intent
import com.mediaapp.MainActivity
import com.mediaapp.album.presentation.AlbumActivity
import com.mediaapp.core.models.Track
import com.mediaapp.core.utils.AlbumLauncher
import com.mediaapp.core.utils.PlaylistHostLauncher
import com.mediaapp.core.utils.PlaylistLauncher
import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.core.utils.UserSearchLauncher
import com.mediaapp.playlist.presentation.playlist_host.PlaylistHostActivity
import com.mediaapp.playlist.presentation.playlist_screen.CurrentPlaylistActivity
import com.mediaapp.playlist.presentation.selected_user_playlists.SelectedUserPlaylistsActivity
import com.mediaapp.registration.utils.RegistrationRouter
import com.mediaapp.user_search.presentation.search_screen.UserSearchActivity

class AppNavigationComponent : RegistrationRouter, AlbumLauncher, PlaylistHostLauncher,
    PlaylistLauncher, UserSearchLauncher, SelectedUserPlaylistsLauncher {
    override fun navigateToMainActivity(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun launchAlbum(context: Context, track: Track) {
        val intent = Intent(context, AlbumActivity::class.java)
        intent.putExtra("track", track)
        context.startActivity(intent)
    }

    override fun launchPlaylistHost(context: Context, track: Track) {
        val intent = Intent(context, PlaylistHostActivity::class.java)
        intent.putExtra("track", track)
        context.startActivity(intent)
    }

    override fun userSearchLaunch(context: Context) {
        val intent = Intent(context, UserSearchActivity::class.java)
        context.startActivity(intent)
    }

    override fun selectedUserPlaylistsLaunch(context: Context, userId: String, userName: String) {
        val intent = Intent(context, SelectedUserPlaylistsActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("username", userName)
        context.startActivity(intent)
    }

    override fun launchPlaylist(playlistId: String, context: Context) {
        val intent = Intent(context, CurrentPlaylistActivity::class.java)
        intent.putExtra("playlistId", playlistId)
        context.startActivity(intent)
    }
}