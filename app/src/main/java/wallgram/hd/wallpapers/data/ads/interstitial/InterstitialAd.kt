package wallgram.hd.wallpapers.data.ads.interstitial

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ActivityContext
import wallgram.hd.wallpapers.BuildConfig
import wallgram.hd.wallpapers.data.IsSubscribed
import wallgram.hd.wallpapers.data.ads.BaseAd
import wallgram.hd.wallpapers.presentation.main.MainActivity
import wallgram.hd.wallpapers.util.modo.Modo
import java.lang.ref.WeakReference
import javax.inject.Inject

interface InterstitialAd {

    fun load()
    fun show()

    class Base @Inject constructor(
        @ActivityContext private val context: Context,
        subscription: IsSubscribed
    ) : BaseAd(subscription), InterstitialAd {

        private var isShowed = false

        private var mInterstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd? = null
        private var mAdIsLoading: Boolean = false

        override fun load() {
            if(!needToLoading())
                return

            if (isShowed)
                return

            val adRequest = AdRequest.Builder().build()

            com.google.android.gms.ads.interstitial.InterstitialAd.load(
                context,
                BuildConfig.INTERSTITIAL_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.d("InterstitialAd", error.message)
                        mInterstitialAd = null
                        mAdIsLoading = false
                    }

                    override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                        Log.d("InterstitialAd", "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        mAdIsLoading = false
                    }
                }
            )
        }

        override fun show() {

            if (isShowed)
                return

            mInterstitialAd?.let { interstitialAd ->
                interstitialAd.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            isShowed = true
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.d("InterstitialAd", "Ad failed to show.")
                            mInterstitialAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            isShowed = true
                            Log.d("InterstitialAd", "Ad showed fullscreen content.")
                        }
                    }
                interstitialAd.show(context as FragmentActivity)
            }
        }
    }


}