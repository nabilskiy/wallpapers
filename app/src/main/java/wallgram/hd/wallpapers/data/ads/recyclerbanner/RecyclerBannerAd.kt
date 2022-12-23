package wallgram.hd.wallpapers.data.ads.recyclerbanner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
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
import wallgram.hd.wallpapers.databinding.ViewAdNativeBannerFullBinding
import javax.inject.Inject

interface RecyclerBannerAd {

    fun adView(): Pair<AdLoader.Builder, ViewBinding>
    fun load(builder: AdLoader.Builder)
    fun adSize(): AdSize

    abstract class Abstract(
        private val displayProvider: DisplayProvider, subscription: IsSubscribed
    ) : BaseAd(subscription), RecyclerBannerAd {

        abstract fun admobId(): String

        override fun adSize(): AdSize =
            displayProvider.adSizeInline()


        override fun load(builder: AdLoader.Builder) {
            if (!needToLoading())
                return

            val adLoader = builder.build()

            adLoader.loadAd(AdRequest.Builder().build())

        }
    }

    class Base @Inject constructor(
        private val context: Context,
        displayProvider: DisplayProvider,
        subscription: IsSubscribed
    ) : Abstract(displayProvider, subscription) {
        override fun admobId() = BuildConfig.NATIVE_BANNER

        override fun adView(): Pair<AdLoader.Builder, ViewAdNativeBannerBinding> {
            val builder = AdLoader.Builder(context, admobId())
            val unifiedAdBinding =
                ViewAdNativeBannerBinding.inflate(LayoutInflater.from(context))
            return Pair(builder, unifiedAdBinding)
        }
    }

    class Carousel @Inject constructor(
        private val context: Context,
        displayProvider: DisplayProvider,
        subscription: IsSubscribed
    ) : Abstract(displayProvider, subscription) {
        override fun admobId() = BuildConfig.NATIVE_CAROUSEL_BANNER

        override fun adView(): Pair<AdLoader.Builder, ViewAdNativeBannerFullBinding> {
            val builder = AdLoader.Builder(context, admobId())
            val unifiedAdBinding = ViewAdNativeBannerFullBinding.inflate(LayoutInflater.from(context))
            return Pair(builder, unifiedAdBinding)
        }
    }

}