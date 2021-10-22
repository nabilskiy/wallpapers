package wallgram.hd.wallpapers.util.localization

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.core.os.ConfigurationCompat
import java.util.*
import android.os.Build

object LanguageSetting {
    private const val PREFERENCE_LANGUAGE = "pref_language"
    private const val KEY_CURRENT_LANGUAGE = "key_language"
    private const val KEY_DEFAULT_LANGUAGE = "key_default_language"

    @JvmStatic
    fun setDefaultLanguage(context: Context, locale: Locale) {
        setPreference(context, KEY_DEFAULT_LANGUAGE, locale.toString())
    }

    @JvmStatic
    fun getDefaultLanguage(context: Context): Locale =
        getPreference(context, KEY_DEFAULT_LANGUAGE)?.let { locale: String ->
            val info = locale.split("_")
            when (info.size) {
                1 -> Locale(info[0])
                2 -> Locale(info[0], info[1])
                3 -> Locale(info[0], info[1], info[2])
                else -> getCurrentLocale(context)
            }
        } ?: run {
            getCurrentLocale(context)
        }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    @JvmStatic
    fun setLanguage(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        setPreference(context, KEY_CURRENT_LANGUAGE, locale.toString())
    }

    @JvmStatic
    fun getLanguage(context: Context): Locale? =
        getPreference(context, KEY_CURRENT_LANGUAGE)?.let { locale: String ->
            val info = locale.split("_")
            when (info.size) {
                1 -> Locale(info[0])
                2 -> Locale(info[0], info[1])
                3 -> Locale(info[0], info[1], info[2])
                else -> null
            }
        } ?: run {
            null
        }

    fun getLanguageWithDefault(context: Context, default: Locale) = getLanguage(context) ?: run {
            setLanguage(context, default)
            default
    }


    private fun setPreference(context: Context, key: String, value: String) {
        context.getSharedPreferences(PREFERENCE_LANGUAGE, Context.MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }

    private fun getPreference(context: Context, key: String, default: String? = null): String? =
        context.getSharedPreferences(PREFERENCE_LANGUAGE, Context.MODE_PRIVATE)
            .getString(key, default)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clear(context: Context) {
        context.getSharedPreferences(PREFERENCE_LANGUAGE, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
