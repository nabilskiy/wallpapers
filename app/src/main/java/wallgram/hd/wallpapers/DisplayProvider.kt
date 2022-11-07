package wallgram.hd.wallpapers

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.resolution.ResolutionCacheDataSource
import wallgram.hd.wallpapers.domain.resolution.ResolutionsInteractor

interface DisplayProvider {

    fun getScreenSize(): Point
    fun getScreen(): String
    fun getScreenSizeUi(): String
    fun getScreenSizeRequest(): String
    fun getWallpaperHeight(): Point


    class Base(
        private var context: Context,
        private val resolutionsCacheDataSource: ResolutionCacheDataSource
    ) : DisplayProvider {
        override fun getScreenSize() =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .run { DisplayMetrics().also { defaultDisplay.getRealMetrics(it) } }
                .run { Point(widthPixels, heightPixels) }

        override fun getScreenSizeUi(): String {
            val screenSize = getScreenSize()
            return "${screenSize.x} x ${screenSize.y}"
        }

        override fun getScreen(): String {
            val screenSize = getScreenSize()
            return "${screenSize.x}x${screenSize.y}"
        }

        override fun getScreenSizeRequest() = resolutionsCacheDataSource.currentResolution()

        override fun getWallpaperHeight(): Point {
            val localPoint: Point = getHeight()
            var i: Int =
                context.resources.getDimensionPixelSize(R.dimen.line_width_normal)
            i = (localPoint.x - i * 2) / 3
            return Point(i, localPoint.y * i / localPoint.x)
        }

        private fun getHeight(): Point {
            val localPoint = Point()
            val window = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            window.defaultDisplay?.getRealSize(localPoint)
            return localPoint
        }


    }

}
