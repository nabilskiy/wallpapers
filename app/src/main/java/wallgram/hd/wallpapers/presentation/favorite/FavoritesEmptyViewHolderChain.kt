package wallgram.hd.wallpapers.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ViewFavoritesEmptyBinding
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class FavoritesEmptyViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.FAVORITES_EMPTY)
            FavoritesEmptyViewHolder(
                ViewFavoritesEmptyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}