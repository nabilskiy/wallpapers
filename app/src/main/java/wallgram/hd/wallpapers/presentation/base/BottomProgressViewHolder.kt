package wallgram.hd.wallpapers.presentation.base

import android.view.View
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.LoadStateFooterViewItemBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class BottomProgressViewHolder(private val binding: LoadStateFooterViewItemBinding) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) = with(binding){
        errorMsg.isVisible = false
        retryButton.isVisible = false
    }
}