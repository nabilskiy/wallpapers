package wallgram.hd.wallpapers

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import wallgram.hd.wallpapers.di.component.DaggerAppComponent
import wallgram.hd.wallpapers.util.LocaleHelper
import wallgram.hd.wallpapers.util.billing.BillingClientLifecycle
import com.bumptech.glide.Glide
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.MultiReducer
import wallgram.hd.wallpapers.util.modo.AppReducer
import wallgram.hd.wallpapers.util.modo.LogReducer
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    val billingClientLifecycle: BillingClientLifecycle
        get() = BillingClientLifecycle.getInstance(this)

    override fun onCreate() {
        wallgram.hd.wallpapers.App.Companion.modo = Modo(LogReducer(AppReducer(this, MultiReducer())))
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> }

        MobileAds.initialize(this)
        MobileAds.setAppMuted(true)
        wallgram.hd.wallpapers.App.Companion.context = applicationContext
        initDagger()
    }

    open fun initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    override fun attachBaseContext(base: Context) {
        wallgram.hd.wallpapers.App.Companion.localeHelper = wallgram.hd.wallpapers.util.LocaleHelper(base)
        super.attachBaseContext(wallgram.hd.wallpapers.App.Companion.localeHelper.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        wallgram.hd.wallpapers.App.Companion.localeHelper.setLocale(this)
    }

    override fun onTrimMemory(level: Int) {
        Glide.with(applicationContext).onTrimMemory(TRIM_MEMORY_MODERATE)
        super.onTrimMemory(level)
    }

    companion object {

        @JvmField
        var isRewardShowed = false

        @JvmField
        var isInterstitialShowed = false

        lateinit var modo: Modo
            private set

        lateinit var localeHelper: wallgram.hd.wallpapers.util.LocaleHelper

        lateinit var context: Context
    }

}