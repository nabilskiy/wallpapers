package wallgram.hd.wallpapers.data.remote

import retrofit2.Response
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.error.NETWORK_ERROR
import wallgram.hd.wallpapers.data.error.NO_INTERNET_CONNECTION
import wallgram.hd.wallpapers.data.remote.service.AkspicService
import wallgram.hd.wallpapers.model.*
import wallgram.hd.wallpapers.util.NetworkConnectivity
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(
        private val serviceGenerator: ServiceGenerator,
        private val networkConnectivity: NetworkConnectivity,
        private val localizationDelegate: LocalizationApplicationDelegate
) : RemoteDataSource {

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }

    override suspend fun getCategories(): Resource<List<Category>> {
        val nasolistService = serviceGenerator.createService(AkspicService::class.java)
        val lang = localizationDelegate.getSupportedLanguage()

        return when (val response =
            processCall {
                nasolistService.getCategories(lang)
            }) {
            is List<*> -> {
                Resource.Success(data = response as List<Category>)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun getWallpapers(sort: String): Resource<ServerResponse<Gallery>> {
        val nasolistService = serviceGenerator.createService(AkspicService::class.java)
        val lang = localizationDelegate.getSupportedLanguage()

        return when (val response =
                processCall {
                    nasolistService.getWallpapersItems(sort, lang = lang, resolution = "1080x1920", page = 1)
                }) {
            is ServerResponse<*> -> {
                Resource.Success(data = response as ServerResponse<Gallery>)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun getSuggest(search: String): Resource<List<String>> {
        val nasolistService = serviceGenerator.createService(AkspicService::class.java)
        val lang = localizationDelegate.getSupportedLanguage()

        return when (val response =
                processCall {
                    nasolistService.getSuggest(search = search, lang = lang)
                }) {
            is List<*> -> {
                Resource.Success(data = response as List<String>)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun getPic(id: Int, res: String): Resource<Pic> {
        val service = serviceGenerator.createService(AkspicService::class.java)
        val lang = localizationDelegate.getSupportedLanguage()
        return when (val response =
                processCall {
                    service.getPic(id, res, lang)
                }) {
            is Pic -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun getTags(isTop: Int): Resource<ServerResponse<Tag>> {
        val nasolistService = serviceGenerator.createService(AkspicService::class.java)
        val lang = localizationDelegate.getSupportedLanguage()

        return when (val response =
                processCall {
                    nasolistService.getTags(is_top = 1, lang = lang, page = 1)
                }) {
            is ServerResponse<*> -> {
                Resource.Success(data = response as ServerResponse<Tag>)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

}