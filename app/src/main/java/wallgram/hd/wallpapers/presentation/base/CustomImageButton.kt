package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class CustomImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr), MyView {

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}