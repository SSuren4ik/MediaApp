package com.mediaapp.design_system

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView

class MusicInAlbumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    private val musicNumber = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.music_name_in_album_size)
    }
    private val musicName = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.music_name_in_album_size)
        setHorizontallyScrolling(true)
    }

    init {
        addView(musicNumber)
        addView(musicName)
        setWillNotDraw(true)
    }

    fun setMusicName(name: String) {
        musicName.text = name
    }

    fun setMusicNumber(number: String) {
        musicNumber.text = number
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        musicName.measure(
            MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.UNSPECIFIED),
            heightMeasureSpec
        )

        val textViewHeight = musicName.measuredHeight

        val numberWidth = (textViewHeight * 1.5).toInt()

        musicNumber.measure(
            MeasureSpec.makeMeasureSpec(numberWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(textViewHeight, MeasureSpec.EXACTLY)
        )

        val textViewWidth = musicName.measuredWidth

        val totalWidth = numberWidth + textViewWidth

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec), resolveSize(numberWidth, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val textViewHeight = musicName.measuredHeight

        val numberWidth = (textViewHeight * 1.5).toInt()

        musicNumber.layout(0, 0, numberWidth, textViewHeight)

        musicName.layout(
            numberWidth, 0, numberWidth + measuredWidth, textViewHeight
        )
    }
}
