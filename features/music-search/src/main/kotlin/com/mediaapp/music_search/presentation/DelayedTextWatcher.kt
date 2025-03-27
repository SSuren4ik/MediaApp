package com.mediaapp.music_search.presentation

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

class DelayedTextWatcher(
    private val delayMillis: Long = 500,
    private val onTextChanged: (String) -> Unit,
) : TextWatcher {

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        searchRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun afterTextChanged(s: Editable?) {
        searchRunnable = Runnable {
            onTextChanged(s.toString())
        }
        handler.postDelayed(searchRunnable!!, delayMillis)
    }
}