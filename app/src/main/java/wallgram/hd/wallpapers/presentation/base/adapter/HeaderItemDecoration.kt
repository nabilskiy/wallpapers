package wallgram.hd.wallpapers.presentation.base.adapter

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class HeaderItemDecoration(
    private val sidePaddingPixels: Int,
    @LayoutRes private val headerViewType: Int
) : ItemDecoration() {
    private fun isHeader(child: View, parent: RecyclerView): Boolean {
        val viewType = parent.layoutManager!!.getItemViewType(child)
        return viewType == headerViewType
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (!isHeader(view, parent)) return
        outRect.left = 16.dp
        outRect.right = 16.dp
        outRect.bottom = 8.dp
    }
}