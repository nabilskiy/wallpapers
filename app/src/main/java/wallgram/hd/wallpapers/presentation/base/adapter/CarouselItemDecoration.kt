package wallgram.hd.wallpapers.presentation.base.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.dp

class CarouselItemDecoration(private val padding: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let { adapter ->
            val childAdapterPosition = parent.getChildAdapterPosition(view)
                .let { if (it == RecyclerView.NO_POSITION) return else it }

            outRect.left = if (childAdapterPosition == 0) 16.dp else 0
            outRect.right = if (childAdapterPosition == adapter.itemCount - 1) 16.dp else 8.dp
        }


    }
}