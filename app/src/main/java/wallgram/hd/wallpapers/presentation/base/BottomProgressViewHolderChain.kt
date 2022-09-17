package wallgram.hd.wallpapers.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.LoadStateFooterViewItemBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.SimpleSpanSizeLookup
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class BottomProgressViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 8)
            BottomProgressViewHolder(
                LoadStateFooterViewItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}