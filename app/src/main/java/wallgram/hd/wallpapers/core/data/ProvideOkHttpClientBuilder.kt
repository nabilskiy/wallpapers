package wallgram.hd.wallpapers.core.data

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface ProvideOkHttpClientBuilder {

    fun httpClientBuilder(): OkHttpClient.Builder

    abstract class Abstract(
        private val provideInterceptor: ProvideInterceptor,
        private val provideLanguageInterceptor: ProvideLanguageInterceptor,
        private val provideResolutionInterceptor: ProvideResolutionInterceptor,
        private val timeOutSeconds: Long = 60L
    ) : ProvideOkHttpClientBuilder {

        override fun httpClientBuilder() = OkHttpClient.Builder()
            .addInterceptor(provideInterceptor.interceptor())
            .addInterceptor(provideLanguageInterceptor.interceptor())
            .addInterceptor(provideResolutionInterceptor.interceptor())
            .connectTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeOutSeconds, TimeUnit.SECONDS)
    }

    class Base(
        provideInterceptor: ProvideInterceptor,
        provideLanguageInterceptor: ProvideLanguageInterceptor,
        provideResolutionInterceptor: ProvideResolutionInterceptor
    ) : Abstract(provideInterceptor, provideLanguageInterceptor, provideResolutionInterceptor)
}