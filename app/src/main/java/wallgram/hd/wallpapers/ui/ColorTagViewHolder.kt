package wallgram.hd.wallpapers.ui

import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.databinding.ItemColorTagBinding


class ColorTagViewHolder(private val itemBinding: ItemColorTagBinding) :
    RecyclerView.ViewHolder(
            itemBinding.root
    ) {
    fun bind(item: Int, onItemClicked: (Int, Int) -> Unit) {
        itemBinding.apply {
            DrawableCompat.setTint(root.background, item)

            root.setOnClickListener {
                onItemClicked.invoke(item, bindingAdapterPosition)
            }
        }
    }
}