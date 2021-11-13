package wallgram.hd.wallpapers.ui.favorite.tags

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class TagsOffsetDecoration(private val offset: Int = 0) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
    ) {

        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams

        outRect.top = 12.dp

        if (layoutParams.spanIndex == 0) {
            outRect.left = 0
            outRect.right = 6.dp
        } else if (layoutParams.spanIndex == 1) {
            outRect.left = 6.dp
            outRect.right = 0.dp
        }

    }
}