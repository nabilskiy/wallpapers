package wallgram.hd.wallpapers.data.search

import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.data.resolution.DisplayResolution
import wallgram.hd.wallpapers.data.gallery.GalleryCloud

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
