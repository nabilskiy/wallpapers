package wallgram.hd.wallpapers.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class SearchEmptyViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 11)
            SearchEmptyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_no_results, parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}