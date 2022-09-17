package wallgram.hd.wallpapers.presentation.filters

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class FilterViewHolder(
    view: View
) : GenericViewHolder<ItemUi>(view) {
    override fun bind(item: ItemUi) = with(itemView) {
        item.show(
            findViewById(R.id.image),
            findViewById(R.id.name)
        )
    }
}