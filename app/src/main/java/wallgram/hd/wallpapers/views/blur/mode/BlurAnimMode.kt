package wallgram.hd.wallpapers.views.blur.mode

import android.widget.ImageView
import wallgram.hd.wallpapers.views.blur.mode.AnimMode
import wallgram.hd.wallpapers.views.blur.mode.BlurAnimMode
import kotlin.math.cos

class BlurAnimMode(private val blurRadius: Int = DEFAULT_BLUR_RADIUS) : AnimMode {

    override fun transformPage(ivBg: ImageView, position: Float, direction: Int) {
        var mFraction = cos(2 * Math.PI * position).toFloat()
        if (mFraction < 0) mFraction = 0f
        ivBg.alpha = mFraction
    }

    override fun blurRadius() = blurRadius

    companion object {
        private const val DEFAULT_BLUR_RADIUS = 100
    }
}