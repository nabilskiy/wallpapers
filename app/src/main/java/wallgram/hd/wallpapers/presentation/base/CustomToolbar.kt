package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.MaterialToolbar
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import java.util.*

class CustomToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialToolbar(context, attrs), MyView {

    override fun show(text: CharSequence) {
        title = text.toString().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

    override fun handleClick(listener: OnClickListener) {
        setNavigationOnClickListener(listener)
    }

}

