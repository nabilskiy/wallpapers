package wallgram.hd.wallpapers.core.data.ads.banner

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

interface AdBanner {

    fun show(adView: AdView)

    class Base: AdBanner {
        override fun show(adView: AdView) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

    }

}