package wallgram.hd.wallpapers.presentation.wallpaper

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class WallpaperItemDecoration(
    @Dimension private val padding: Int,
    private val headerViewType: Int
) : ItemDecoration() {

    private fun isValid(child: View, parent: RecyclerView): Boolean {
        val viewType = parent.layoutManager!!.getItemViewType(child)
        return viewType == headerViewType
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isValid(view, parent)) return

        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams

        outRect.bottom = padding

        if (layoutParams.spanIndex == 0) {
            outRect.left = 0
            outRect.right = padding
        } else if (layoutParams.spanIndex == 2) {
            outRect.left = padding
            outRect.right = 0
        }

    }
}