package wallgram.hd.wallpapers.data.ads.recyclerbanner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import wallgram.hd.wallpapers.BuildConfig
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.IsSubscribed
import wallgram.hd.wallpapers.data.ads.BaseAd
import wallgram.hd.wallpapers.databinding.ViewAdNativeBannerBinding
import javax.inject.Inject

interface RecyclerBannerAd {

    fun adView(): Pair<AdLoader.Builder, ViewAdNativeBannerBinding>
    fun load(builder: AdLoader.Builder)
    fun adSize(): AdSize

    abstract class Abstract(
        private val context: Context,
        private val displayProvider: DisplayProvider, subscription: IsSubscribed
    ) : BaseAd(subscription), RecyclerBannerAd {

        abstract fun admobId(): String

        override fun adSize(): AdSize =
            displayProvider.adSizeInline()

        override fun adView(): Pair<AdLoader.Builder, ViewAdNativeBannerBinding> {
            val builder = AdLoader.Builder(context, admobId())
            val unifiedAdBinding = ViewAdNativeBannerBinding.inflate(LayoutInflater.from(context))
            return Pair(builder, unifiedAdBinding)
        }

        override fun load(builder: AdLoader.Builder) {
            if (!needToLoading())
                return

            val adLoader = builder.build()

            adLoader.loadAd(AdRequest.Builder().build())

        }
    }

    class Base @Inject constructor(
        context: Context,
        displayProvider: DisplayProvider,
        subscription: IsSubscribed
    ) : Abstract(context, displayProvider, subscription) {
        override fun admobId() = BuildConfig.NATIVE_BANNER
    }

    class Carousel @Inject constructor(
        context: Context,
        displayProvider: DisplayProvider,
        subscription: IsSubscribed
    ) : Abstract(context, displayProvider, subscription) {
        override fun admobId() = BuildConfig.NATIVE_CAROUSEL_BANNER
    }

}