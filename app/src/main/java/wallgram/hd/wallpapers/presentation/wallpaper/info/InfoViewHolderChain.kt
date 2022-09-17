package wallgram.hd.wallpapers.presentation.wallpaper.info

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemInfoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*
import wallgram.hd.wallpapers.presentation.filters.FilterUi

class InfoViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 9)
            InfoViewHolder(
                ItemInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}