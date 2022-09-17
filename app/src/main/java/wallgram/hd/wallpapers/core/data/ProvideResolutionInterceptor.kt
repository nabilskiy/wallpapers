package wallgram.hd.wallpapers.core.data

import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.remote.ResolutionInterceptor

interface ProvideResolutionInterceptor {

    fun interceptor(): ResolutionInterceptor

    class Base(private val displayProvider: DisplayProvider) :
        ProvideResolutionInterceptor {
        override fun interceptor(): ResolutionInterceptor =
            ResolutionInterceptor(displayProvider)
    }
}