package wallgram.hd.wallpapers.util.downloadx.helper

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url
import wallgram.hd.wallpapers.data.remote.moshiFactories.MyKotlinJsonAdapterFactory

internal const val FAKE_BASE_URL = "http://www.example.com"

internal fun apiCreator(client: OkHttpClient): Api {
    val retrofit = Retrofit.Builder()
        .baseUrl(FAKE_BASE_URL)
        .client(client)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(MyKotlinJsonAdapterFactory())
                    .add(wallgram.hd.wallpapers.data.remote.moshiFactories.MyStandardJsonAdapters.FACTORY)
                    .build()
            )
        )
        .build()
    return retrofit.create(Api::class.java)
}

internal interface Api {

    @GET
    @Streaming
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Response<ResponseBody>
}