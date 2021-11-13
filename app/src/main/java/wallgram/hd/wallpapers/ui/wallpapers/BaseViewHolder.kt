package wallgram.hd.wallpapers.ui.wallpapers

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun performBind(item :T?)
}