package wallgram.hd.wallpapers.data.history

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.domain.favorites.FavoritesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.history.HistoryRepository
import javax.inject.Inject

class BaseHistoryRepository @Inject constructor(
    private val cacheDataSource: FavoritesCacheDataSource,
    private val mapper: GalleryCache.Mapper<GalleryData>,
    private val domainMapper: GalleryData.Mapper<GalleryDomain>,
    private val cache: WallpapersCache.Mutable,
) :
    HistoryRepository {

    private val dataList = ArrayList<GalleryData>()

    override suspend fun history(): GalleriesDomain {
        val favorites = cacheDataSource.history().map { it.map(mapper) }
        if (favorites.isNotEmpty()) {
            dataList.addAll(favorites)}

        return GalleriesDomain.Base(favorites.map { it.map(domainMapper) }, favorites.isEmpty())
    }

    override fun save(wallpaperRequest: WallpaperRequest, position: Int) {
        val wallpaperCache = WallpaperCache.Base(dataList, position, wallpaperRequest)
        cache.save(wallpaperCache)
    }

    override fun read(): GalleriesDomain {
//        val list = cache.read().list()
        return GalleriesDomain.Base(listOf(), false)
    }

    override fun position() = cache.read().position()

}

