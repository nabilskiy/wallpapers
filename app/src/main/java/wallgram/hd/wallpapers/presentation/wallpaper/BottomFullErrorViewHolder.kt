package wallgram.hd.wallpapers.presentation.wallpaper

import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.ViewErrorBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class BottomFullErrorViewHolder(private val binding: ViewErrorBinding) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) = with(binding) {
        repeatButton.isVisible = false
    }
}