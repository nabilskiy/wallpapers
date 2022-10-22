package wallgram.hd.wallpapers.data.filters

import retrofit2.http.GET
import wallgram.hd.wallpapers.data.resolution.DisplayResolution

interface FiltersService {

    @DisplayResolution
    @GET("filters?channel=wallgram-main&sample=30")
    suspend fun filters(): List<FiltersCloud.Base>

    @DisplayResolution
    @GET("filters?channel=wallspic-app-category&sample=10")
    suspend fun categories(): List<FiltersCloud.Base>
}
