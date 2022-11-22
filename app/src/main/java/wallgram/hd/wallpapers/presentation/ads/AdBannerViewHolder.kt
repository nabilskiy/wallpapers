package wallgram.hd.wallpapers.presentation.ads

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemAdBannerBinding
import wallgram.hd.wallpapers.databinding.ItemAdBannerFullBinding
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class AdBannerViewHolder(
    private val binding: ItemAdBannerBinding
) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.root)
    }
}

class AdBannerFullViewHolder(
    private val binding: ItemAdBannerFullBinding
) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.root)
    }
}