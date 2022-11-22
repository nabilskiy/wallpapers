package wallgram.hd.wallpapers.presentation.subscribe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding
import wallgram.hd.wallpapers.databinding.ItemSubscriptionErrorBinding
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.categories.HeaderViewHolder

class SubscriptionErrorUi(
    private val title: String
) : ItemUi {
    override fun type() = ViewIds.SUB_ERROR

    override fun show(vararg views: MyView) {
        views[0].show(title)
    }

    override fun id() = title

    override fun content() = title
}

class SubscriptionErrorViewHolder(
    private val binding: ItemSubscriptionErrorBinding
) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.errorText)
    }
}

class SubscriptionErrorViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.SUB_ERROR)
            SubscriptionErrorViewHolder(
                ItemSubscriptionErrorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)

}