package wallgram.hd.wallpapers.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.resolution.DisplayResolution
import javax.inject.Inject

class ResolutionInterceptor @Inject constructor(
    private val displayProvider: DisplayProvider
) : Interceptor {

    private companion object {
        const val PARAMETER_NAME = "resolution"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val invocation = originalRequest.tag(Invocation::class.java)
        val newAuth = invocation?.method()?.getAnnotation(DisplayResolution::class.java)
        val url = originalRequest.url.newBuilder()

        if (newAuth != null)
            url.addQueryParameter(PARAMETER_NAME, displayProvider.getScreenSizeRequest())

        val requestBuilder: Request.Builder = originalRequest.newBuilder()
            .url(url.build())
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
