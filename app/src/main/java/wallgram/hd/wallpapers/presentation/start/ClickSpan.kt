package wallgram.hd.wallpapers.presentation.start

import android.text.style.ClickableSpan
import android.view.View

class ClickSpan(private val onClick: () -> Unit) : ClickableSpan() {

    override fun onClick(widget: View) {
        onClick.invoke()
    }
}
