package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import java.util.*

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), TextContainer, MyView {

    override fun show(data: String) {
        text = data.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

    override fun show(text: CharSequence) {
        setText(text)
    }

    override fun textSize(size: Float) {
        textSize = size
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}

