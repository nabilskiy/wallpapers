package wallgram.hd.wallpapers.presentation.subscribe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemSubscriptionBinding
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.subscribe.Subscription
import wallgram.hd.wallpapers.presentation.subscribe.buy.BuySubscriptions

class SubscriptionContentUi(
    private val subs: List<Subscription>
) : ItemUi {
    override fun type() = ViewIds.SUB_CONTENT

    override fun show(vararg views: MyView) {
        views[0].subscriptions(subs)
//        views[0].handleClick{
//            buySubscriptions.map("1month")
//        }
    }

    override fun id() = "sub_content"

    override fun content() = "sub_content"
}

class SubscriptionContentViewHolder(
    private val binding: ItemSubscriptionBinding
) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.root)
    }
}

class SubscriptionContentViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.SUB_CONTENT) SubscriptionContentViewHolder(
            ItemSubscriptionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else viewHolderFactoryChain.viewHolder(parent, viewType)

}