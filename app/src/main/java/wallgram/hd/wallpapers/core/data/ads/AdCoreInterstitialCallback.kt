package wallgram.hd.wallpapers.core.data.ads

interface AdCoreInterstitialCallback {

    fun onShowAdRequestProgress(tag: String, message: String)

    fun onHideAdRequestProgress(tag: String, message: String)

    fun onAdDismissed(tag: String, message: String)

    fun onAdFailed(tag: String, errorMessage: String)

    fun onAdLoaded(tag: String, message: String)

    fun onAdShowed(tag: String, message: String)

}