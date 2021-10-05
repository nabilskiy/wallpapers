package wallgram.hd.wallpapers.util.localization

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

object LocalizationUtility {
    fun getLocalizedContext(baseContext: Context): Context {
        val (configuration, isChanged) = getLocalizedConfiguration(baseContext, baseContext.resources.configuration)
        return when {
            isChanged ->
                baseContext.createConfigurationContext(configuration)
            else -> baseContext
        }
    }

    fun getLocalizedResources(baseContext: Context, baseResources: Resources): Resources {
        val (configuration, isChanged) = getLocalizedConfiguration(baseContext, baseResources.configuration)
        return when {
            isChanged ->
                baseContext.createConfigurationContext(configuration).resources
            else -> baseResources
        }
    }

    fun getLocalizedConfiguration(baseContext: Context, baseConfiguration: Configuration): Pair<Configuration, Boolean> {
        val defaultLocale = LanguageSetting.getDefaultLanguage(baseContext)
        val currentLocale = LanguageSetting.getLanguageWithDefault(baseContext, defaultLocale)
        val baseLocale = getLocaleFromConfiguration(baseConfiguration)
        if (!isRequestedLocaleChanged(baseLocale, currentLocale)) return baseConfiguration to false
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val localeList = LocaleList(currentLocale)
                LocaleList.setDefault(localeList)
                Configuration(baseConfiguration).apply {
                    setLocale(currentLocale)
                    setLocales(localeList)
                } to true
            }
            else -> {
                Configuration(baseConfiguration).apply {
                    setLocale(currentLocale)
                } to true
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getLocaleFromConfiguration(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            configuration.locales.get(0) ?: Locale.getDefault()
        } else {
            configuration.locale
        }
    }

    private fun isRequestedLocaleChanged(baseLocale: Locale, currentLocale: Locale) =
        !baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)
}
