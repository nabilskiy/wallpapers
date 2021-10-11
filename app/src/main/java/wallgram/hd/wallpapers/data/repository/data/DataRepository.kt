package wallgram.hd.wallpapers.data.repository.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.local.dao.GalleryDao
import wallgram.hd.wallpapers.data.remote.RemoteData
import wallgram.hd.wallpapers.model.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(
        private val remoteRepository: RemoteData,
        private val galleryDao: GalleryDao,
        private val ioDispatcher: CoroutineContext
) : DataRepositorySource {

    override suspend fun getCategories(): Flow<Resource<List<Category>>> {
        return flow {
            emit(remoteRepository.getCategories())
        }.flowOn(ioDispatcher)
    }

    override suspend fun getTags(isTop: Int): Flow<Resource<ServerResponse<Tag>>> {
        return flow {
            emit(remoteRepository.getTags(isTop))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getWallpapers(sort: String): Flow<Resource<ServerResponse<Gallery>>> {
        return flow {
            emit(remoteRepository.getWallpapers(sort))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getSuggest(search: String): Flow<Resource<List<String>>> {
        return flow {
            emit(remoteRepository.getSuggest(search))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getPic(id: Int, res: String): Flow<Resource<Pic>> {
        return flow{
            emit(remoteRepository.getPic(id, res))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getSavedItems(type: Int): Flow<List<Gallery>> = galleryDao.getAllWithType(type)
    override suspend fun addToFavorites(gallery: Gallery) {
        galleryDao.addToFavorites(gallery)
    }

    override suspend fun isFavorite(id: Int): Flow<Boolean> {
        return flow {
            emit(galleryDao.isFavorite(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun saveItem(gallery: Gallery) {
        galleryDao.insertOrUpdate(gallery)
    }

    override suspend fun deleteItem(gallery: Gallery) {
        galleryDao.delete(gallery)
    }

    override suspend fun deleteAllItems(type: Int) {
        galleryDao.deleteAll(type)
    }

}
