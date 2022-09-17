package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.data.colors.Color

class ColorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), MyView {

    override fun show(text: CharSequence) {
        setText(text)
    }

    override fun show(textId: Int) {
        setText(textId)
    }

    override fun textColor(color: Int) {
        setTextColor(ContextCompat.getColor(context, color))
    }

    override fun setGradientDrawable(startColor: Int, endColor: Int) {
        val colorList = IntArray(2) { ContextCompat.getColor(context, startColor) }

        val newBackground =
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.color_tag_background,
                null
            ) as GradientDrawable

        newBackground.apply {
            mutate()
            colorList[colorList.size - 1] = ContextCompat.getColor(context, endColor)
            colors = colorList
        }

        background = newBackground
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}