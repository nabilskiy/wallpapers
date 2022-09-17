package wallgram.hd.wallpapers.util.linktextview.internal

import android.view.View

interface OnLinkClickListener {
    fun onClick(view: View, content: String)
}