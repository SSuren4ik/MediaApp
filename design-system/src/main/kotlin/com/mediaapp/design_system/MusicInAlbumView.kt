package com.mediaapp.design_system

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView

class MusicInAlbumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    private val musicNumber = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.music_name_in_album_size)
        textAlignment = TEXT_ALIGNMENT_CENTER
    }

    private val musicName = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.music_name_in_album_size)
        isSingleLine = false
        ellipsize = TextUtils.TruncateAt.END
    }

    private val numberWidthFixed = resources.getDimensionPixelSize(R.dimen.number_in_music_size)
    private val totalHeightKf = 1.7f

    init {
        addView(musicNumber)
        addView(musicName)
        setWillNotDraw(false)
    }

    fun setMusicName(name: String) {
        musicName.text = name
    }

    fun setMusicNumber(number: String) {
        musicNumber.text = number
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        musicName.measure(
            MeasureSpec.makeMeasureSpec(parentWidth - numberWidthFixed, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val textViewHeight = musicName.measuredHeight

        musicNumber.measure(
            MeasureSpec.makeMeasureSpec(numberWidthFixed, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(textViewHeight, MeasureSpec.EXACTLY)
        )

        val totalWidth = numberWidthFixed + musicName.measuredWidth
        val totalHeight = (textViewHeight * totalHeightKf).toInt()

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val textViewHeight = musicName.measuredHeight
        val totalHeight = (textViewHeight * totalHeightKf).toInt()
        val verticalOffset = (totalHeight - textViewHeight) / 2

        musicNumber.layout(0, verticalOffset, numberWidthFixed, verticalOffset + textViewHeight)
        musicName.layout(
            numberWidthFixed,
            verticalOffset,
            numberWidthFixed + musicName.measuredWidth,
            verticalOffset + textViewHeight
        )
    }
}
