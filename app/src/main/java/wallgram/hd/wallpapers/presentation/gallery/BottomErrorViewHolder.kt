package wallgram.hd.wallpapers.presentation.gallery

import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.LoadStateFooterViewItemBinding
import wallgram.hd.wallpapers.databinding.ViewErrorBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class BottomErrorViewHolder(private val binding: LoadStateFooterViewItemBinding) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) = with(binding) {
        errorMsg.isVisible = true
        retryButton.isVisible = true
        progressBar.isVisible = false
    }
}