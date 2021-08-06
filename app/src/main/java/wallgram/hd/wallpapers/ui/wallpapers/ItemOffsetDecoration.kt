package wallgram.hd.wallpapers.ui.wallpapers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class ItemOffsetDecoration  (private val offset: Int = 0) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
    ) {

        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams

        parent.adapter?.let { adapter ->
            outRect.top = when(parent.getChildAdapterPosition(view)){
                RecyclerView.NO_POSITION -> 0
                0, 1, 2 -> offset.dp
                else -> 0
            }
            outRect.bottom = when(parent.getChildAdapterPosition(view)){
                RecyclerView.NO_POSITION -> 0
                adapter.itemCount - 1, adapter.itemCount - 2, adapter.itemCount - 3 -> 24.dp
                else -> 2.dp
            }
        }

        if (layoutParams.spanIndex == 0) {
            outRect.left = 0
            outRect.right = 2.dp
        } else if (layoutParams.spanIndex == 1) {
            outRect.left = 0
            outRect.right = 2.dp
        }

    }
}