package wallgram.hd.wallpapers.data.remote

import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.*

internal interface RemoteDataSource {

    suspend fun getCategories(): Resource<List<Category>>
    suspend fun getWallpapers(sort: String): Resource<ServerResponse<Gallery>>
    suspend fun getSuggest(search: String, lang: String): Resource<List<String>>
    suspend fun getPic(id: Int, res: String, lang: String): Resource<Pic>

    suspend fun getTags(isTop: Int, lang: String): Resource<ServerResponse<Tag>>
}