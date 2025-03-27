package com.mediaapp.design_system

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class PlaylistView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    val iconImage = ImageView(context).apply {
        layoutParams = LayoutParams(
            resources.getDimension(R.dimen.playlist_icon_size).toInt(),
            resources.getDimension(R.dimen.playlist_icon_size).toInt()
        )
        setBackgroundResource(R.drawable.gray_rounded_background)
    }

    private val playlistNameTextView = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.album_name_under_album_size)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )
        setBackgroundResource(R.drawable.gray_rounded_background)
        isSingleLine = true
        typeface = ResourcesCompat.getFont(context, R.font.roboto)
    }

    private val artistNameTextView = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.artist_name_under_album_size)
        setTextColor(resources.getColor(R.color.gray, context.theme))
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )
        setBackgroundResource(R.drawable.gray_rounded_background)
        isSingleLine = true
        typeface = ResourcesCompat.getFont(context, R.font.roboto)
    }

    init {
        addView(iconImage)
        addView(playlistNameTextView)
        addView(artistNameTextView)
        setWillNotDraw(true)
    }

    fun setPlaylistName(text: String) {
        playlistNameTextView.text = text
        playlistNameTextView.setBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    fun setAuthorName(text: String) {
        artistNameTextView.text = text
        artistNameTextView.setBackgroundColor(resources.getColor(android.R.color.transparent))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val iconSize = resources.getDimension(R.dimen.playlist_icon_size).toInt()

        iconImage.measure(
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
        )

        val textWidthSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(widthMeasureSpec) - iconSize, MeasureSpec.EXACTLY
        )

        val playlistNameTextHeight = (playlistNameTextView.textSize * 1.5).toInt()

        playlistNameTextView.measure(
            textWidthSpec, MeasureSpec.makeMeasureSpec(playlistNameTextHeight, MeasureSpec.EXACTLY)
        )
        artistNameTextView.measure(
            textWidthSpec, MeasureSpec.makeMeasureSpec(playlistNameTextHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            resolveSize(iconSize + playlistNameTextView.measuredWidth, widthMeasureSpec),
            resolveSize(iconSize, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val iconSize = resources.getDimension(R.dimen.playlist_icon_size).toInt()
        val textPadding = resources.getDimensionPixelSize(R.dimen.playlist_text_left_margin)
        iconImage.layout(0, 0, iconSize, iconSize)

        val textWidth = r - l - iconSize - textPadding

        val playlistNameTop = iconSize / 4
        playlistNameTextView.layout(
            iconSize + textPadding,
            playlistNameTop,
            iconSize + textWidth,
            playlistNameTop + playlistNameTextView.measuredHeight
        )

        val artistNameTop = playlistNameTop + playlistNameTextView.measuredHeight
        artistNameTextView.layout(
            iconSize + textPadding,
            artistNameTop,
            iconSize + textWidth,
            artistNameTop + artistNameTextView.measuredHeight
        )
    }
}
