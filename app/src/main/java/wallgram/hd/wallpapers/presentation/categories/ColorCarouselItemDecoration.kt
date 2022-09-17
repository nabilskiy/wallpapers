package wallgram.hd.wallpapers.presentation.categories

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class ColorCarouselItemDecoration(
    private val sidePaddingPixels: Int,
    private val headerViewType: Int
) : ItemDecoration() {
    private fun isColorCarousel(child: View, parent: RecyclerView): Boolean {
        val viewType = parent.layoutManager!!.getItemViewType(child)
        return viewType == headerViewType
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isColorCarousel(view, parent)) return

        outRect.top = 12.dp
        outRect.bottom = 12.dp
    }
}