package com.mediaapp.design_system

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AlbumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    val iconImage = ImageView(context).apply {
        layoutParams = LayoutParams(
            resources.getDimension(R.dimen.album_icon_size).toInt(),
            resources.getDimension(R.dimen.album_icon_size).toInt()
        )
    }

    private val albumNameTextView = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.album_name_under_album_size)
        setHorizontallyScrolling(true)
        isSingleLine = true
        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        isSelected = true
    }

    private val artistNameTextView = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.artist_name_under_album_size)
        setTextColor(resources.getColor(R.color.gray, context.theme))
        setHorizontallyScrolling(true)
        isSingleLine = true
        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        isSelected = true
    }

    init {
        addView(iconImage)
        addView(albumNameTextView)
        addView(artistNameTextView)
        setWillNotDraw(true)
    }

    fun setAlbumName(text: String) {
        albumNameTextView.text = text
    }

    fun setArtistName(text: String) {
        artistNameTextView.text = text
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val iconSize = resources.getDimension(R.dimen.album_icon_size).toInt()

        iconImage.measure(
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
        )

        albumNameTextView.measure(
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        artistNameTextView.measure(
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val totalHeight =
            iconSize + albumNameTextView.measuredHeight + artistNameTextView.measuredHeight

        setMeasuredDimension(
            resolveSize(iconSize, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val iconSize = resources.getDimension(R.dimen.album_icon_size).toInt()

        iconImage.layout(0, 0, iconSize, iconSize)

        val albumNameTop = iconSize
        albumNameTextView.layout(
            0, albumNameTop, iconSize, albumNameTop + albumNameTextView.measuredHeight
        )

        val artistNameTop = albumNameTop + albumNameTextView.measuredHeight
        artistNameTextView.layout(
            0, artistNameTop, iconSize, artistNameTop + artistNameTextView.measuredHeight
        )
    }
}