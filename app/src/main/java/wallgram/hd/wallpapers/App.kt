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
import wallgram.hd.wallpapers.data.ads.appopen.AppOpenAdManager
import wallgram.hd.wallpapers.data.ads.appopen.OnShowAdCompleteListener
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.util.localization.LanguageSetting.getDefaultLanguage
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver, Application.ActivityLifecycleCallbacks {

    fun showAd(activity: Activity, listener: OnShowAdCompleteListener){
        appOpenAdManager.showAdIfAvailable(activity, listener)
    }

    private val localizationDelegate = LocalizationApplicationDelegate(this)

    private var currentActivity: Activity? = null

    private lateinit var appOpenAdManager: AppOpenAdManager

    override fun onCreate() {
        modo = Modo(LogReducer(AppReducer(this, MultiReducer())))
        super<Application>.onCreate()

        registerActivityLifecycleCallbacks(this)

//        MobileAds.setRequestConfiguration(
//            RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("F54E65E1F95728FDA49814E0D2CEBB65")).build()
//        )

        MobileAds.initialize(this) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("5DFEFD9479D0674E03C1C60DAFD07107")).build()
        )

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        appOpenAdManager = AppOpenAdManager.Base()
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

    override fun onStart(owner: LifecycleOwner) {
        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) {
        if (appOpenAdManager.isShowingAd())
            currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit


    override fun getResources() = localizationDelegate.getResources(this, super.getResources())

    companion object {

        lateinit var modo: Modo
            private set

    }

}