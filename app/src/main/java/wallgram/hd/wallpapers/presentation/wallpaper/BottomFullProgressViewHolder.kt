package wallgram.hd.wallpapers.presentation.wallpaper

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.LoadStateFooterViewItemBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class BottomFullProgressViewHolder(private val binding: LoadStateFooterViewItemBinding) : GenericViewHolder<ItemUi>(binding.root) {

    init {
        itemView.layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    override fun bind(item: ItemUi) = with(binding){
        errorMsg.isVisible = false
        retryButton.isVisible = false
    }
}