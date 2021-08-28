package wallgram.hd.wallpapers

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import wallgram.hd.wallpapers.di.component.DaggerAppComponent
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
import wallgram.hd.wallpapers.util.localization.LanguageSetting.getDefaultLanguage
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import java.util.*
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun onCreate() {
        modo = Modo(LogReducer(AppReducer(this, MultiReducer())))
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> }

        MobileAds.initialize(this)
        MobileAds.setAppMuted(true)
        context = applicationContext
        initDagger()
    }

    open fun initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this)
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

        lateinit var context: Context
    }

}