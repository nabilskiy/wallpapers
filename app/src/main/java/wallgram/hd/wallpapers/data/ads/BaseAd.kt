package wallgram.hd.wallpapers.data.ads

import wallgram.hd.wallpapers.data.IsSubscribed

abstract class BaseAd(private val isSubscribed: IsSubscribed) : Ad {
    override fun needToLoading() = !isSubscribed.isSubscribed()
}