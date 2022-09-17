package wallgram.hd.wallpapers.presentation.subscribe

import wallgram.hd.wallpapers.core.Mapper

interface SubscriptionUi {

    fun map(mapper: Mapper.Unit<List<Subscription>>)

    class Base(
        private val listSku: List<Subscription>
    ) : SubscriptionUi {

        override fun map(mapper: Mapper.Unit<List<Subscription>>) {
            mapper.map(listSku)
        }

    }
}
