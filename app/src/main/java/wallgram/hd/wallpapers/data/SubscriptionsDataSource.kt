package wallgram.hd.wallpapers.data

import wallgram.hd.wallpapers.presentation.subscribe.ChangeSubscription

interface SubscriptionsDataSource : ChangeSubscription, IsSubscribed {

    class Base() : SubscriptionsDataSource {

        private var isSubscribed = false

        override fun changeSubscription(subscribed: Boolean): Boolean {
            isSubscribed = subscribed
            return isSubscribed
        }

        override fun isSubscribed() = isSubscribed

    }

}