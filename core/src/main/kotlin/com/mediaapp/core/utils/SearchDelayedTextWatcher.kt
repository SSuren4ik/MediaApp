package com.mediaapp.core.utils

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchDelayedTextWatcher(
    private val delayMillis: Long = 500,
    private val coroutineScope: CoroutineScope,
    private val onTextChanged: (String) -> Unit,
) : TextWatcher {

    private var job: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        job?.cancel()
    }

    override fun afterTextChanged(s: Editable?) {
        job = coroutineScope.launch {
            delay(delayMillis)
            onTextChanged(s.toString())
        }
    }
}