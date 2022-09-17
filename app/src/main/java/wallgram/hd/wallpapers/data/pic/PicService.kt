package wallgram.hd.wallpapers.data.pic

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.data.DisplayResolution
import wallgram.hd.wallpapers.model.ServerResponse

interface PicService {

    @DisplayResolution
    @GET("pic")
    suspend fun pic(
        @Query("id") id: Int,
    ): PicCloud.Base



}
