package com.mediaapp.design_system

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MusicListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    val iconImage = ImageView(context)
    private val textView = TextView(context).apply {
        textSize = resources.getDimension(R.dimen.small_text_size)
        setHorizontallyScrolling(true)
        isSingleLine = true
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        isSelected = true
    }

    private val textMargin = resources.getDimensionPixelSize(R.dimen.track_name_image_margin)

    init {
        addView(iconImage)
        addView(textView)
        setWillNotDraw(true)
    }

    fun setText(text: String) {
        textView.text = text
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textView.measure(
            MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.UNSPECIFIED),
            heightMeasureSpec
        )

        val textViewHeight = textView.measuredHeight
        val iconSize = (textViewHeight * 1.5).toInt()

        iconImage.measure(
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
        )

        val textViewWidth = textView.measuredWidth
        val totalWidth = iconSize + textMargin + textViewWidth

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec), resolveSize(iconSize, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val textViewHeight = textView.measuredHeight
        val iconSize = (textViewHeight * 1.5).toInt()

        iconImage.layout(0, 0, iconSize, iconSize)

        val textViewTop = (iconSize - textViewHeight) / 2
        val textLeft = iconSize + textMargin

        textView.layout(
            textLeft, textViewTop, textLeft + textView.measuredWidth, textViewTop + textViewHeight
        )
    }
}