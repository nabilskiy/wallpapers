package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import java.util.*

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), MyView {

    private fun toLowerCase(value: String): String = value.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    override fun show(text: CharSequence) {
        setText(toLowerCase(text.toString()))
    }

    override fun show(textId: Int) {
        setText(textId)
    }

    override fun textSize(size: Float) {
        textSize = size
    }

    override fun textAlignment(alignment: Int){
        textAlignment = alignment
    }

    override fun showImageResource(id: Int) {
        setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
    }

    override fun showStartImageResource(id: Int) {
        setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}

