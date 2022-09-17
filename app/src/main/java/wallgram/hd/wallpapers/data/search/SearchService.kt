package wallgram.hd.wallpapers.data.search

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.data.DisplayResolution
import wallgram.hd.wallpapers.data.gallery.GalleryCloud
import wallgram.hd.wallpapers.model.ServerResponse

interface SearchService {

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun search(
        @Query("search") query: String,
        @Query("page") page: Int
    ): List<GalleryCloud.Base>

    @GET("suggest")
    suspend fun getSuggest(
        @Query("search") search: String
    ): List<String>
}
