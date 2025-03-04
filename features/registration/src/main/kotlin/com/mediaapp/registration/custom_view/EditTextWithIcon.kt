package com.mediaapp.registration.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import kotlin.math.max

class EditTextWithIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr) {

    private val iconImage = ImageView(context)
    private val editText = EditText(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        textSize = resources.getDimension(com.mediaapp.design_system.R.dimen.small_text_size)
    }

    init {
        addView(iconImage)
        addView(editText)
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth =
            iconImage.measuredWidth + editText.measuredWidth + paddingLeft + paddingRight
        val desiredHeight =
            max(iconImage.measuredHeight, editText.measuredHeight) + paddingTop + paddingBottom

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val iconWidth = iconImage.measuredWidth
        val iconHeight = iconImage.measuredHeight
        val editTextWidth = editText.measuredWidth
        val editTextHeight = editText.measuredHeight

        val iconLeft = paddingLeft
        val iconTop = (height - iconHeight) / 2
        val iconRight = iconLeft + iconWidth
        val iconBottom = iconTop + iconHeight

        val editTextLeft = iconRight + 20
        val editTextTop = (height - editTextHeight) / 2
        val editTextRight = editTextLeft + editTextWidth
        val editTextBottom = editTextTop + editTextHeight

        iconImage.layout(iconLeft, iconTop, iconRight, iconBottom)
        editText.layout(editTextLeft, editTextTop, editTextRight, editTextBottom)
    }

    fun setImageResource(resId: Int) {
        iconImage.setImageResource(resId)
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setInputType(type: Int) {
        editText.inputType = type
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun setText(text: String) {
        editText.setText(text)
    }
}
