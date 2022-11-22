package wallgram.hd.wallpapers.views

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.RelativeLayout

class WindowInsetsRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {

    override fun onApplyWindowInsets(windowInsets: WindowInsets): WindowInsets {
        childCount.let {
            for (index in 0 until it) {
                getChildAt(index).dispatchApplyWindowInsets(windowInsets)
            }
        }
        return windowInsets
    }
}