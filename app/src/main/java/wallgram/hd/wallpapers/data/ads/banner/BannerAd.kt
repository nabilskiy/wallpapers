package wallgram.hd.wallpapers.data.ads.banner

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.core.view.isNotEmpty
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dagger.hilt.android.qualifiers.ActivityContext
import wallgram.hd.wallpapers.BuildConfig
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.IsSubscribed
import wallgram.hd.wallpapers.data.ads.Ad
import wallgram.hd.wallpapers.data.ads.BaseAd
import wallgram.hd.wallpapers.data.ads.interstitial.InterstitialAd
import wallgram.hd.wallpapers.util.doOnApplyWindowInsets
import wallgram.hd.wallpapers.util.dp
import javax.inject.Inject

interface BannerAd : DefaultLifecycleObserver {

    fun show(container: ViewGroup)
    fun adSize(): AdSize

    class Base @Inject constructor(
        context: Context,
        private val displayProvider: DisplayProvider,
        subscription: IsSubscribed
    ) : BaseAd(subscription), BannerAd {

        override fun adSize(): AdSize = if (needToLoading())
            displayProvider.adSize() else AdSize(-1, 30)

        private var initialLayoutComplete = false

        private val adView: AdView = AdView(context)

        private fun load() {
            val adRequest = AdRequest.Builder().build()

            adView.apply {
                adUnitId = BuildConfig.BANNER_ID
                setAdSize(displayProvider.adSize())
                loadAd(adRequest)

            }

        }

        override fun show(container: ViewGroup) {
            if(!needToLoading())
                return

            if (container.isNotEmpty())
                container.removeAllViews()

            if (adView.parent != null) {
                (adView.parent as ViewGroup).removeView(adView)
            }

            container.addView(adView)

            container.viewTreeObserver.addOnGlobalLayoutListener {

                if (!initialLayoutComplete) {
                    initialLayoutComplete = true
                    load()
                }
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            adView.pause()
            super.onPause(owner)
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            adView.resume()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            adView.destroy()
            super.onDestroy(owner)
        }

    }

}