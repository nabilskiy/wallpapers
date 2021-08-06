package wallgram.hd.wallpapers.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.model.*

interface AkspicService {
    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersItems(@Query("sort") sort: String, @Query("page") page: Int, @Query("resolution") resolution: String, @Query("lang") lang: String): Response<ServerResponse<Gallery>>

    @GET("tags?amount=10")
    suspend fun getTags(@Query("is_top") is_top: Int, @Query("page") page: Int, @Query("lang") lang: String): Response<ServerResponse<Tag>>

    @GET("categories")
    suspend fun getCategories(@Query("lang") lang: String): Response<List<Category>>

    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersFromCategory(@Query("category") category: Int, @Query("sort") sort: String, @Query("page") page: Int, @Query("resolution") resolution: String, @Query("lang") lang: String): Response<ServerResponse<Gallery>>

    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersFromTag(@Query("tag") tag: Int, @Query("sort") sort: String, @Query("page") page: Int, @Query("resolution") resolution: String, @Query("lang") lang: String): Response<ServerResponse<Gallery>>

    @GET("gallery?amount=27&safe=1")
    suspend fun search(@Query("search") query: String, @Query("page") page: Int, @Query("resolution") resolution: String, @Query("lang") lang: String): Response<ServerResponse<Gallery>>

    @GET("pic")
    suspend fun getPic(@Query("id") id: Int, @Query("resolution") resolution: String, @Query("lang") lang: String): Response<Pic>

    @GET("similar?amount=27")
    suspend fun getSimilar(@Query("pic") id: Int, @Query("resolution") resolution: String, @Query("lang") lang: String, @Query("page") page: Int): Response<ServerResponse<Gallery>>

    @GET("suggest")
    suspend fun getSuggest(@Query("search") search: String, @Query("lang") lang: String): Response<List<String>>
}