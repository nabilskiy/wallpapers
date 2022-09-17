package wallgram.hd.wallpapers.data.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import javax.inject.Inject

class LanguageInterceptor @Inject constructor(
    private val locale: LocalizationApplicationDelegate
) : Interceptor {

    private companion object {
        const val PARAMETER_NAME = "lang"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter(PARAMETER_NAME, locale.getSupportedLanguage())
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
