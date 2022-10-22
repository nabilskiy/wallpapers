package wallgram.hd.wallpapers.presentation.settings.resolution

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.CustomCheckBox
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi


class ResolutionViewHolder(view: View) : GenericViewHolder<ItemUi>(view) {

    override fun bind(item: ItemUi) {
        item.show(
            itemView.findViewById<CustomCheckBox>(R.id.res_item)
        )
    }
}