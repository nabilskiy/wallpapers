package wallgram.hd.wallpapers.presentation.gallery

import android.graphics.Point
import android.view.ViewGroup
import wallgram.hd.wallpapers.util.dp

interface GalleryViewType {

    fun size(): Point
    fun corners(): Float = 0f

    class Default : GalleryViewType {
        override fun size() =
            Point(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    class Custom(private val width: Int, private val height: Int) : GalleryViewType {
        override fun size() = Point(width, height)
    }

    class Small : GalleryViewType {
        override fun size() = Point(124.dp, 204.dp)
        override fun corners() = 4f.dp
    }

    class Full : GalleryViewType {
        override fun size() = Point(ViewGroup.LayoutParams.MATCH_PARENT, 100.dp)
    }

}
