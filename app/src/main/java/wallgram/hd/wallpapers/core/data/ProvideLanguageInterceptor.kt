package wallgram.hd.wallpapers.core.data

import wallgram.hd.wallpapers.data.remote.LanguageInterceptor
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import javax.inject.Inject

interface ProvideLanguageInterceptor {

    fun interceptor(): LanguageInterceptor

    class Base @Inject constructor(private val localizationApplicationDelegate: LocalizationApplicationDelegate) :
        ProvideLanguageInterceptor {
        override fun interceptor(): LanguageInterceptor =
            LanguageInterceptor(localizationApplicationDelegate)
    }
}