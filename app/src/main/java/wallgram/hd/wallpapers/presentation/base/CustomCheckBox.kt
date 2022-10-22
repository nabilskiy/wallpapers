package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import java.util.*

class CustomCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatCheckBox(context, attrs), TextContainer, MyView {

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

    override fun check(checked: Boolean) {
        isChecked = checked
    }

}

