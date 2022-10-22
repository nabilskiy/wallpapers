package wallgram.hd.wallpapers.data.ads.appopen

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import wallgram.hd.wallpapers.BuildConfig
import java.util.*

interface AppOpenAdManager {

    fun load(activity: Activity)
    fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean
    fun isAdAvailable(): Boolean
    fun showAdIfAvailable(activity: Activity)
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener)
    fun isShowingAd(): Boolean

    class Base(): AppOpenAdManager{

        private val LOG = "AppOpenAd"

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        private var isShowingAd = false

        private var loadTime: Long = 0

        override fun isShowingAd() = isShowingAd

        override fun load(activity: Activity) {
            if(isLoadingAd || isAdAvailable())
                return

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                activity,
                BuildConfig.APP_OPEN_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback(){
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Log.d(LOG, "onAdLoaded.")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Log.d(LOG, "onAdFailedToLoad: " + loadAdError.message)
                    }
                }
            )
        }

        override fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        override fun isAdAvailable() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

        override fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                    }
                }
            )
        }

        override fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            if (isShowingAd) {
                Log.d(LOG, "The app open ad is already showing.")
                return
            }

            if (!isAdAvailable()) {
                Log.d(LOG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                load(activity)
                return
            }

            Log.d(LOG, "Will show ad.")

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(LOG, "onAdDismissedFullScreenContent.")

                    onShowAdCompleteListener.onShowAdComplete()
                    load(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(LOG, "onAdFailedToShowFullScreenContent: " + adError.message)

                    onShowAdCompleteListener.onShowAdComplete()
                    load(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(LOG, "onAdShowedFullScreenContent.")
                }
            }
            isShowingAd = true
            appOpenAd!!.show(activity!!)
        }

    }

}