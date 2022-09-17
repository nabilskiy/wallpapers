package wallgram.hd.wallpapers.presentation

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.subscribe.Subscription

interface SkuMapper : Mapper.Unit<Subscription> {
    class Empty : SkuMapper {
        override fun map(data: Subscription) = Unit
    }
}
