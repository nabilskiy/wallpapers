package wallgram.hd.wallpapers.presentation.subscribe

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.subscribe.adapter.SubscriptionContentUi
import wallgram.hd.wallpapers.presentation.subscribe.adapter.SubscriptionErrorUi
import wallgram.hd.wallpapers.presentation.subscribe.adapter.SubscriptionFeatureUi
import wallgram.hd.wallpapers.presentation.subscribe.adapter.SubscriptionHeaderUi
import wallgram.hd.wallpapers.presentation.subscribe.buy.BuySubscriptions
import javax.inject.Inject


interface SubscriptionsDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val gallery: List<Subscription>
    ) : SubscriptionsDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(gallery)
    }

    class Empty : SubscriptionsDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(listOf())
    }

    interface Mapper<T> {
        fun map(
            source: List<Subscription>
        ): T

        class Base @Inject constructor(
            private val resourceProvider: ResourceProvider
        ) : Mapper<SubscriptionsUi> {

            override fun map(
                source: List<Subscription>
            ): SubscriptionsUi {
                val result = mutableListOf<ItemUi>()

                //result.add(ProgressUi())

                when {
//                    source.isEmpty() -> {
//                        result.add(SubscriptionHeaderUi(resourceProvider.string(R.string.wallgram_pro)))
//                        result.addAll(
//                            listOf(
//                                SubscriptionFeatureUi(resourceProvider.string(R.string.no_ads_100)),
//                                SubscriptionFeatureUi(resourceProvider.string(R.string.unlimited_wallpaper_downloads)),
//                                SubscriptionFeatureUi(resourceProvider.string(R.string.daily_wallpapers_update)),
//                                SubscriptionFeatureUi(resourceProvider.string(R.string.full_access_to_the_wallpaper_changer))
//                            )
//                        )
//                        result.add(SubscriptionErrorUi(resourceProvider.string(R.string.error_get_prices)))
//                    }
                    source.any { it.map(Subscription.Mapper.Purchased()) } -> {
                        result.add(SubscriptionHeaderUi(resourceProvider.string(R.string.use_wallgram_pro)))

                        val purchased = source.find { it.map(Subscription.Mapper.Purchased()) }
                            ?: Subscription.Empty()
                        val formattedDate = purchased.map(Subscription.Mapper.Date())
                            .toString("dd.MM.yyyy")
                        val text =
                            resourceProvider.string(R.string.subscription_purchased, formattedDate)

                        result.add(
                            SubscriptionFeatureUi(
                                text,
                                0,
                                View.TEXT_ALIGNMENT_CENTER
                            )
                        )
                    }
                    else -> {
                        result.add(SubscriptionHeaderUi(resourceProvider.string(R.string.wallgram_pro)))
                        result.addAll(
                            listOf(
                                SubscriptionFeatureUi(resourceProvider.string(R.string.no_ads_100)),
                                SubscriptionFeatureUi(resourceProvider.string(R.string.unlimited_wallpaper_downloads)),
                                SubscriptionFeatureUi(resourceProvider.string(R.string.daily_wallpapers_update)),
                                SubscriptionFeatureUi(resourceProvider.string(R.string.full_access_to_the_wallpaper_changer))
                            )
                        )
                        result.add(SubscriptionContentUi(source))
                    }


                }


                return SubscriptionsUi.Base(result)
            }
        }

    }
}