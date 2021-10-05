package wallgram.hd.wallpapers.util.localization

import android.content.Context
import android.content.res.Resources
import wallgram.hd.wallpapers.App.Companion.context
import wallgram.hd.wallpapers.util.localization.LocalizationUtility
import java.util.*

class LocalizationApplicationDelegate {

    fun onConfigurationChanged(context: Context) = LocalizationUtility.getLocalizedContext(context)

    fun attachBaseContext(context: Context): Context = LocalizationUtility.getLocalizedContext(context)

    fun getApplicationContext(applicationContext: Context): Context = LocalizationUtility.getLocalizedContext(applicationContext)

    fun getResources(appContext: Context, resources: Resources): Resources = LocalizationUtility.getLocalizedResources(appContext, resources)

    fun setDefaultLanguage(context: Context, language: String) {
        val locale = Locale(language)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, language: String, country: String) {
        val locale = Locale(language, country)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, locale: Locale) {
        LanguageSetting.setDefaultLanguage(context, locale)
    }

    fun getLanguage(): Locale {
        val defaultLocale = LanguageSetting.getDefaultLanguage(context)
        return LanguageSetting.getLanguageWithDefault(context, defaultLocale)
    }

    fun getSupportedLanguage(): String{
        val defaultLocale = LanguageSetting.getDefaultLanguage(context)
        val locale = LanguageSetting.getLanguageWithDefault(context, defaultLocale)
        return if(supportedLanguages.contains(locale.language)) locale.language else "en"
    }

    private val supportedLanguages = listOf("en", "es", "zh", "de", "fr", "uk", "pt", "ru")
}