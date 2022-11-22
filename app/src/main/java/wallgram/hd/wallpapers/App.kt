package wallgram.hd.wallpapers

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import wallgram.hd.wallpapers.data.ads.appopen.AppOpenAdManager
import wallgram.hd.wallpapers.data.ads.appopen.OnShowAdCompleteListener
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.util.localization.LanguageSetting.getDefaultLanguage
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    private val localizationDelegate = LocalizationApplicationDelegate(this)

    @Inject
    lateinit var appOpenAdManager: AppOpenAdManager


    override fun onCreate() {
        modo = Modo(LogReducer(AppReducer(this, MultiReducer())))
        super.onCreate()

        MobileAds.initialize(this) {}

//        MobileAds.setRequestConfiguration(
//            RequestConfiguration.Builder()
//                .setTestDeviceIds(
//                    Arrays.asList(
//                        "13CA781B0CB755D6A2D0AFEAD81ABB89",
//                        "843065C72AD2F89D76EEB1B67506650A"
//                    )
//                ).build()
//        )

        appOpenAdManager.init()
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


    override fun getResources() = localizationDelegate.getResources(this, super.getResources())

    companion object {

        lateinit var modo: Modo
            private set

    }

}