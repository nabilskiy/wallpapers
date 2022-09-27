package wallgram.hd.wallpapers.data.filters

import retrofit2.http.GET

interface FiltersService {
    @GET("filters?channel=wallgram-main&sample=30")
    suspend fun filters(): List<FiltersCloud.Base>

    @GET("filters?channel=wallspic-app-category&sample=10")
    suspend fun categories(): List<FiltersCloud.Base>
}
