package wallgram.hd.wallpapers.presentation.wallpaper.info

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemInfoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.filters.FilterUi

class InfoViewHolder(
    private val binding: ItemInfoBinding
) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.authorTitle, binding.authorValue)
    }
}