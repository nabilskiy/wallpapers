package wallgram.hd.wallpapers

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.MultiReducer
import wallgram.hd.wallpapers.util.modo.AppReducer
import wallgram.hd.wallpapers.util.modo.LogReducer
import com.google.android.gms.ads.appopen.AppOpenAd
import dagger.hilt.android.HiltAndroidApp
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.util.localization.LanguageSetting.getDefaultLanguage
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import java.util.*
import javax.inject.Inject

private const val AD_UNIT_ID = "ca-app-pub-3722478150829941/8708499260"

@HiltAndroidApp
class App : Application(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    private var isAdsShowed: Boolean = false
    private var isUserSubscribe: Boolean = false

    @Inject
    lateinit var billingRepository: BillingRepository

    private val localizationDelegate =  LocalizationApplicationDelegate(this)

    override fun onCreate() {
        modo = Modo(LogReducer(AppReducer(this, MultiReducer())))
        super.onCreate()

        billingRepository.getCurrentSub().asLiveData().observeForever {
            isUserSubscribe = it != DEFAULT_SKU
        }

        registerActivityLifecycleCallbacks(this)
        MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("A9616546D7D5AE93B41FBE292574762E")).build())

        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()


    }


    override fun onTrimMemory(level: Int) {
        Glide.with(applicationContext).onTrimMemory(TRIM_MEMORY_MODERATE)
        super.onTrimMemory(level)
    }

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, getDefaultLanguage(base))
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(it)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }


    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}


    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }


    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        private var loadTime: Long = 0

        fun loadAd(context: Context) {

            if (isLoadingAd || isAdAvailable()) {
                return
            }

            if(isUserSubscribe)
                return

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                    }
                })
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        private fun isAdAvailable(): Boolean {
            return false
            //return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4) && !isUserSubscribe
        }

        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                    }
                })
        }

        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener
        ) {

            if (isAdsShowed) {
                return
            }

            if (isShowingAd) {
                return
            }

            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    isAdsShowed = true

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                }
            }
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }

    override fun getResources() = localizationDelegate.getResources(this, super.getResources())

    companion object {

        lateinit var modo: Modo
            private set

    }

}