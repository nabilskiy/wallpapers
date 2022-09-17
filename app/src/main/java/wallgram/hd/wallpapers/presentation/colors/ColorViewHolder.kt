package wallgram.hd.wallpapers.presentation.colors

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.filters.FilterUi

class ColorViewHolder(
    view: View,
) : GenericViewHolder<ItemUi>(view) {
    override fun bind(item: ItemUi) {
        item.show(itemView as MyView)
    }
}