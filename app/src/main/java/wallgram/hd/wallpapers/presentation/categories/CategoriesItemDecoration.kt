package wallgram.hd.wallpapers.presentation.categories

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class CategoriesItemDecoration(
    @LayoutRes
    private val headerViewType: Int
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
    ) {
        if(!isValid(view, parent))
            return

        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams

        outRect.bottom = 12.dp
        if (layoutParams.spanIndex % 2 == 0) {
            outRect.left = 16.dp
            outRect.right = 4.dp
        } else {
            outRect.left = 4.dp
            outRect.right = 16.dp
        }

    }

    private fun isValid(child: View, parent: RecyclerView): Boolean {
        val viewType = parent.layoutManager?.getItemViewType(child)
        return viewType == headerViewType
    }
}
