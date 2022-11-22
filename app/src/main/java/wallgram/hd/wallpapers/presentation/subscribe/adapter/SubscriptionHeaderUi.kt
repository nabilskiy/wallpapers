package wallgram.hd.wallpapers.presentation.subscribe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding
import wallgram.hd.wallpapers.databinding.ItemSubscriptionHeaderBinding
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.categories.HeaderViewHolder

class SubscriptionHeaderUi(
    private val title: String
) : ItemUi {
    override fun type() = ViewIds.SUB_HEADER

    override fun show(vararg views: MyView) {
        views[0].show(title)
    }

    override fun id() = title

    override fun content() = title
}

class SubscriptionHeaderViewHolder(
    private val binding: ItemSubscriptionHeaderBinding
) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.titleText)
    }
}

class SubscriptionHeaderViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.SUB_HEADER)
            SubscriptionHeaderViewHolder(
                ItemSubscriptionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)

}