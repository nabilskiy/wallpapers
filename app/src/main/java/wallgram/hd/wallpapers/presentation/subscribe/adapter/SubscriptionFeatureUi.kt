package wallgram.hd.wallpapers.presentation.subscribe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.joda.time.DateTime
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemSubscriptionFeatureBinding
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class SubscriptionFeatureUi(
    private val feature: String,
    private val icon: Int = R.drawable.ic_check_small,
    private val alignment: Int = View.TEXT_ALIGNMENT_TEXT_START
) : ItemUi {
    override fun type() = ViewIds.SUB_FEATURES

    override fun show(vararg views: MyView) = with(views[0]) {
        show(feature)
        showStartImageResource(icon)
        textAlignment(alignment)
    }

    override fun id() = feature

    override fun content() = feature
}

class SubscriptionFeatureViewHolder(
    private val binding: ItemSubscriptionFeatureBinding
) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.featureText)
    }
}

class SubscriptionFeatureViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == ViewIds.SUB_FEATURES) SubscriptionFeatureViewHolder(
            ItemSubscriptionFeatureBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else viewHolderFactoryChain.viewHolder(parent, viewType)

}