package wallgram.hd.wallpapers.util.localization

import android.app.Service
import android.content.Context
import android.content.res.Resources
import wallgram.hd.wallpapers.util.localization.LocalizationUtility
import java.util.*

open class LocalizationServiceDelegate(private val service: Service) {
    fun getResources(resources: Resources): Resources {
        return LocalizationUtility.getLocalizedResources(service, resources)
    }

    fun getLanguage(): Locale {
        val defaultLocale = LanguageSetting.getDefaultLanguage(service)
        return LanguageSetting.getLanguageWithDefault(service, defaultLocale)
    }

    fun getBaseContext(baseContext: Context): Context {
        return LocalizationUtility.getLocalizedContext(baseContext)
    }

    fun getApplicationContext(applicationContext: Context): Context {
        return LocalizationUtility.getLocalizedContext(applicationContext)
    }
}
