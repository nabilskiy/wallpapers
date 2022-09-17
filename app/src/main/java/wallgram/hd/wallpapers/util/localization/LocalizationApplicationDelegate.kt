package wallgram.hd.wallpapers.util.localization

import android.content.Context
import android.content.res.Resources
import java.util.*

class LocalizationApplicationDelegate(private val context: Context) {

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


    fun getSupportedLanguage(): String{
        val defaultLocale = LanguageSetting.getDefaultLanguage(context)
        val locale = LanguageSetting.getLanguageWithDefault(context, defaultLocale)
        return if(supportedLanguages.contains(locale.language)) locale.language else "en"
    }

    private val supportedLanguages = listOf("en", "es", "zh", "de", "fr", "uk", "pt", "ru")
}

