package wallgram.hd.wallpapers.presentation.subscribe.adapter

import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain

class SubscriptionAdapter : GenericAdapter.Base(
    ProgressViewHolderChain(
        SubscriptionHeaderViewHolderChain(
            SubscriptionFeatureViewHolderChain(
                SubscriptionContentViewHolderChain(
                    SubscriptionErrorViewHolderChain(
                        ViewHolderFactoryChain.Exception()
                    )
                )
            )
        )
    )
)