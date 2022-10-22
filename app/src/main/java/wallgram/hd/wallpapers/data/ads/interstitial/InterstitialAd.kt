package wallgram.hd.wallpapers.data.ads.interstitial

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import wallgram.hd.wallpapers.BuildConfig
import java.lang.ref.WeakReference

interface InterstitialAd {

    fun load()
    fun show()

    abstract class Abstract(activity: Activity) : InterstitialAd {
        protected val activityWeakReference = WeakReference(activity)

        protected val TAG = "InterstitialAd"
    }

    class Base(activity: Activity) : Abstract(activity) {

        private var isShowed = false

        private var mInterstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd? = null
        private var mAdIsLoading: Boolean = false

        override fun load() {
            if(isShowed)
                return

            val adRequest = AdRequest.Builder().build()

            val context = activityWeakReference.get() ?: return

            com.google.android.gms.ads.interstitial.InterstitialAd.load(
                context,
                BuildConfig.INTERSTITIAL_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.d(TAG, error.message)
                        mInterstitialAd = null
                        mAdIsLoading = false
                    }

                    override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                        Log.d(TAG, "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        mAdIsLoading = false
                    }
                }
            )
        }

        override fun show() {
            if(isShowed)
                return

            mInterstitialAd?.let { interstitialAd ->
                interstitialAd.fullScreenContentCallback =
                    object: FullScreenContentCallback(){
                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            load()
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.d(TAG, "Ad failed to show.")
                            mInterstitialAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            isShowed = true
                            Log.d(TAG, "Ad showed fullscreen content.")
                        }
                    }
                val context = activityWeakReference.get() ?: return
                interstitialAd.show(context)
            }
        }
    }


}