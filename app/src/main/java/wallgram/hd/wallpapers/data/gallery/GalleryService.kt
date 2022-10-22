package wallgram.hd.wallpapers.data.gallery

import retrofit2.http.GET
import retrofit2.http.Query
import wallgram.hd.wallpapers.data.resolution.DisplayResolution
import wallgram.hd.wallpapers.model.ServerResponse

interface GalleryService {

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersItems(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("filter") filter: Int? = null
    ): ServerResponse<GalleryCloud.Base>

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersItemsFromColor(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("primary_color[r]") r: Int,
        @Query("primary_color[g]") g: Int,
        @Query("primary_color[b]") b: Int,
        @Query("primary_color_threshold") threshold: Int = 45
    ): ServerResponse<GalleryCloud.Base>

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersFromCategory(
        @Query("filter") filter: Int,
        @Query("sort") sort: String,
        @Query("page") page: Int
    ): ServerResponse<GalleryCloud.Base>

    @DisplayResolution
    @GET("similar?amount=27&safe=1")
    suspend fun getSimilarWallpapers(
        @Query("pic") pic: Int,
        @Query("page") page: Int
    ): ServerResponse<GalleryCloud.Base>

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun getWallpapersFromTag(
        @Query("tag") tag: Int,
        @Query("sort") sort: String,
        @Query("page") page: Int
    ): ServerResponse<GalleryCloud.Base>

    @DisplayResolution
    @GET("gallery?amount=27&safe=1")
    suspend fun search(
        @Query("search") query: String,
        @Query("page") page: Int
    ): ServerResponse<GalleryCloud.Base>


}
