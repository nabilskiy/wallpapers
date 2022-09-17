package wallgram.hd.wallpapers.core.data.ads.interstitial

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import wallgram.hd.wallpapers.BuildConfig

interface AdInterstitial {

    fun load(activity: Activity, callback: AdInterstitialCallback?)
    fun show(activity: Activity)


    class Base() : AdInterstitial {
        val TAG: String = Base::class.java.simpleName

        private var mInterstitialAd: InterstitialAd? = null

        override fun load(activity: Activity, callback: AdInterstitialCallback?) {
            val adRequest = AdRequest.Builder()

            InterstitialAd.load(activity, BuildConfig.INTERSTITIAL_ID, adRequest.build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mInterstitialAd = null
                        callback?.onAdFailed(TAG, "Interstitial ${adError.message}")
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd

                        callback?.onAdLoaded(TAG, "Interstitial Ad was loaded")

                        interstitialAd.fullScreenContentCallback = object :
                            FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                mInterstitialAd = null
                                callback?.onAdDismissed(TAG, "Interstitial Ad was dismissed")
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                mInterstitialAd = null
                                callback?.onAdFailed(TAG, "Interstitial Ad failed to show")
                            }

                            override fun onAdShowedFullScreenContent() {
                                callback?.onAdShowed(
                                    TAG,
                                    "Interstitial Ad showed fullscreen content"
                                )
                            }
                        }
                    }
                })
        }



        override fun show(activity: Activity) {
            mInterstitialAd?.show(activity)
        }

    }

}