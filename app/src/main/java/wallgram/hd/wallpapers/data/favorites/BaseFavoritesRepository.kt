package wallgram.hd.wallpapers.data.favorites

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.*
import wallgram.hd.wallpapers.domain.favorites.FavoritesRepository
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import javax.inject.Inject

class BaseFavoritesRepository @Inject constructor(
    private val cacheDataSource: FavoritesCacheDataSource,
    private val mapper: GalleryCache.Mapper<GalleryData>,
    private val domainMapper: GalleryData.Mapper<GalleryDomain>,
    private val cache: WallpapersCache.Mutable,
) :
    FavoritesRepository {

    private val dataList = ArrayList<GalleryData>()

    override suspend fun favorites(): GalleriesDomain {
        val favorites = cacheDataSource.favorites().map { it.map(mapper) }
        if (favorites.isNotEmpty()) {
            dataList.addAll(favorites)
        }

        val isEmpty = favorites.isEmpty() || favorites.size < 27

        return GalleriesDomain.Base(favorites.map { it.map(domainMapper) }, isEmpty)
    }

    override suspend fun history(): GalleriesDomain {
        val favorites = cacheDataSource.history().map { it.map(mapper) }
        if (favorites.isNotEmpty()) {
            dataList.addAll(favorites)
        }

        val isEmpty = favorites.isEmpty() || favorites.size < 27

        return GalleriesDomain.Base(favorites.map { it.map(domainMapper) }, isEmpty)
    }

    override fun save(wallpaperRequest: WallpaperRequest, position: Int) {
        val wallpaperCache = WallpaperCache.Base(position, wallpaperRequest)
        cache.save(wallpaperCache)
    }

    override fun read(): GalleriesDomain {
//        val list = cache.read().list()

        return GalleriesDomain.Base(listOf(), true)
    }


}

