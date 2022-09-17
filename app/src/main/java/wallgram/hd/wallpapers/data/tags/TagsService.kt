package wallgram.hd.wallpapers.data.tags

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.model.ServerResponse
import wallgram.hd.wallpapers.model.Tag

interface TagsService {

    @GET("tags")
    suspend fun getTags(
        @Query("is_top") is_top: Int,
        @Query("page") page: Int,
        @Query("amount") amount: Int = 10,
    ): Response<ServerResponse<Tag>>

}
