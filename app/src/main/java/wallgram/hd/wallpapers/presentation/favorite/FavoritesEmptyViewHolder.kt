package wallgram.hd.wallpapers.presentation.favorite

import android.view.View
import wallgram.hd.wallpapers.databinding.ViewFavoritesEmptyBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class FavoritesEmptyViewHolder(private val binding: ViewFavoritesEmptyBinding) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) = item.show(binding.text)
}