package wallgram.hd.wallpapers.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class ColorsItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(rect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.let { adapter ->
            rect.left = when(parent.getChildAdapterPosition(view)){
                RecyclerView.NO_POSITION -> 0
                0 -> 16.dp
                else -> 8.dp
            }
            rect.right = when (parent.getChildAdapterPosition(view)) {
                RecyclerView.NO_POSITION -> 0
                adapter.itemCount - 1 -> 16.dp
                else -> 0
            }
        }
    }
}