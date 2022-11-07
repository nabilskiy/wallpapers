package wallgram.hd.wallpapers.views.blur.mode

import android.widget.ImageView

interface AnimMode {

    fun transformPage(ivBg: ImageView, position: Float, direction: Int)
    fun blurRadius(): Int

    companion object {
        const val D_LEFT = -1
        const val D_RIGHT = 1
    }
}