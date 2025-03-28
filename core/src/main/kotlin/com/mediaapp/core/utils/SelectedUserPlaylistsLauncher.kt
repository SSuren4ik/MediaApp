package com.mediaapp.core.utils

import android.content.Context

interface SelectedUserPlaylistsLauncher {
    fun selectedUserPlaylistsLaunch(context: Context, userId: String, userName: String)
}