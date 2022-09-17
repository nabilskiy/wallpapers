package wallgram.hd.wallpapers.presentation.categories

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class ItemOffsetDecoration :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
    ) {

        if (parent.getChildAdapterPosition(view) == 0)
            return

        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams

        outRect.top = 0
        outRect.bottom = 12.dp
        if (layoutParams.spanIndex % 2 == 0) {
            outRect.left = 0
            outRect.right = 4.dp
        } else {
            outRect.left = 4.dp
            outRect.right = 0
        }

    }
}