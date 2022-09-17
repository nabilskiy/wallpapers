package wallgram.hd.wallpapers.presentation.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.*

class FilterViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 1)
            FilterViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category, parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}