package wallgram.hd.wallpapers.data.pic

import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.data.resolution.DisplayResolution

interface PicService {

    @DisplayResolution
    @GET("pic")
    suspend fun pic(
        @Query("id") id: Int,
    ): PicCloud.Base


}
