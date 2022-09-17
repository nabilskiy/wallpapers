package wallgram.hd.wallpapers.presentation.home

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class HomeItemDecoration(
    private val padding: Int,
    private val headerViewType: Int
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
        outRect.bottom = padding
    }
}