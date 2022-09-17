package wallgram.hd.wallpapers.core.data

import wallgram.hd.wallpapers.data.remote.LanguageInterceptor
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate

interface ProvideLanguageInterceptor {

    fun interceptor(): LanguageInterceptor

    class Base(private val localizationApplicationDelegate: LocalizationApplicationDelegate) :
        ProvideLanguageInterceptor {
        override fun interceptor(): LanguageInterceptor =
            LanguageInterceptor(localizationApplicationDelegate)
    }
}