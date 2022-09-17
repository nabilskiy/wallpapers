package wallgram.hd.wallpapers.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class SearchViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 10)
            SearchViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_search, parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}