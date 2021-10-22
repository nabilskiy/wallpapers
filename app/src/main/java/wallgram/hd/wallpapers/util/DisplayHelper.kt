package wallgram.hd.wallpapers.util

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager
import javax.inject.Inject

class DisplayHelper @Inject constructor(val context: Context) {

    private val physicalScreenRectPx: Rect
        get() =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .run { DisplayMetrics().also { defaultDisplay.getRealMetrics(it) } }
                .run { Rect(0, 0, widthPixels, heightPixels) }

    fun getPhysicalScreenResolution(): String {
        val widthPx = physicalScreenRectPx.width()
        val heightPx = physicalScreenRectPx.height()
        return widthPx.toString() + "x" + heightPx.toString()
    }

}