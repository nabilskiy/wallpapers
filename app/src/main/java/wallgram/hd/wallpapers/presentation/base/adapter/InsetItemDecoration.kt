package wallgram.hd.wallpapers.presentation.base.adapter

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class InsetItemDecoration(
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

        val position = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }

        if (position % 2 == 0) {
            outRect.left = padding / 4
            outRect.right = padding
            outRect.bottom = padding / 2
        } else {
            outRect.left = padding
            outRect.right = padding / 4
            outRect.bottom = padding / 2
        }
    }
}