package wallgram.hd.wallpapers.core.data.ads.banner

interface AdBannerCallback {
    fun onAdLoaded(tag: String, message: String)
    fun onAdFailedToLoad(tag: String, errorCode: String, errorMessage: String)
    fun onAdOpened(tag: String, message: String)
    fun onAdClicked(tag: String, message: String)
    fun onAdClosed(tag: String, message: String)
}