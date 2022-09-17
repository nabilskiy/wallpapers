package wallgram.hd.wallpapers.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.LoadStateFooterViewItemBinding
import wallgram.hd.wallpapers.databinding.ViewErrorBinding
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.SimpleSpanSizeLookup
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class BottomErrorViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 12)
            BottomErrorViewHolder(
                LoadStateFooterViewItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}