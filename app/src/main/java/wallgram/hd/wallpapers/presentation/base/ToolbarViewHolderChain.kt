package wallgram.hd.wallpapers.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding
import wallgram.hd.wallpapers.databinding.ViewToolbarBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*

class ToolbarViewHolderChain(
    private val clickListener: GenericAdapter.ClickListener<Unit>,
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.TOOLBAR)
            ToolbarViewHolder(
                ViewToolbarBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                clickListener
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)

}