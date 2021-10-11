package wallgram.hd.wallpapers.data.repository.data

import kotlinx.coroutines.flow.Flow
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.*


interface DataRepositorySource {

    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getTags(isTop: Int): Flow<Resource<ServerResponse<Tag>>>

    suspend fun getWallpapers(sort: String): Flow<Resource<ServerResponse<Gallery>>>
    suspend fun getSuggest(search: String): Flow<Resource<List<String>>>
    suspend fun getPic(id: Int, res: String): Flow<Resource<Pic>>

    suspend fun getSavedItems(type: Int): Flow<List<Gallery>>

    suspend fun addToFavorites(gallery: Gallery)
    suspend fun isFavorite(id: Int): Flow<Boolean>
    suspend fun saveItem(gallery: Gallery)
    suspend fun deleteItem(gallery: Gallery)
    suspend fun deleteAllItems(type: Int)

}