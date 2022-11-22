package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.LoggingHashMap
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.data.filters.FiltersCloudDataSource
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.home.HomeDomain
import wallgram.hd.wallpapers.domain.home.HomesDomain
import javax.inject.Inject

class BaseGalleryRepository @Inject constructor(
    private val cloudDataSource: GalleryCloudDataSource,
    private val cacheDataSource: FavoritesCacheDataSource,
    private val filtersCloudDataSource: FiltersCloudDataSource,
    private val filtersMapper: FiltersCloud.Mapper<HomeDomain>,
    private val cache: WallpapersCache.Mutable,
    private val mapper: GalleryCloud.Mapper<GalleryData>,
    private val cacheMapper: GalleryCache.Mapper<GalleryData>,
    private val domainMapper: GalleryData.Mapper<GalleryDomain>
) : GalleryRepository {

    private var search = ""

    private var wallpapers = LoggingHashMap<String, WallpaperRequest>()

    override fun save(id: Int, requestId: String) {
        val request = wallpapers.getOrElse(requestId) { WallpaperRequest.DATE() }
        cache.save(WallpaperCache.Base(id, request))
    }

    override suspend fun filters(): HomesDomain {
        val filters = filtersCloudDataSource.filters()

        if (filters.isNotEmpty()) {
            filters.forEach { filter ->
                val item = filter.map(FiltersCloud.Mapper.Test())
                wallpapers[item.itemId()] = item
            }
        }

        return HomesDomain.Base(filters.map { it.map(filtersMapper) })
    }

    override suspend fun gallery(request: WallpaperRequest): GalleriesDomain {
        updateSearch(request)

        val gallery = cloudDataSource.gallery(request)
        val list = gallery.list.map { it.map(mapper) }
        if (list.isNotEmpty()) {
            if (wallpapers.containsKey(request.itemId())) {
                val data = ArrayList(wallpapers.getValue(request.itemId()).data())
                data.addAll(list)
                wallpapers[request.itemId()]?.setData(data)
            } else
                wallpapers[request.itemId()] = request.setData(list)

            request.nextPage()
        }
        val isEmpty = list.isEmpty()
        val data = wallpapers.getOrElse(request.itemId()) { WallpaperRequest.DATE() }.data()
        return GalleriesDomain.Base(
            data.map { it.map(domainMapper, requestId = request.itemId()) },
            isEmpty
        )
    }

    override suspend fun favorites(): GalleriesDomain {
        val favorites = cacheDataSource.favorites().map { it.map(cacheMapper) }
        val request = WallpaperRequest.FAVORITES().setData(favorites)
        wallpapers[request.itemId()] = request
        return GalleriesDomain.Base(
            request.data().map { it.map(domainMapper, requestId = request.itemId()) },
            true
        )
    }

    override suspend fun history(): GalleriesDomain {
        val history = cacheDataSource.history().map { it.map(cacheMapper) }
        val request = WallpaperRequest.History().setData(history)
        wallpapers[request.itemId()] = request
        return GalleriesDomain.Base(
            request.data().map { it.map(domainMapper, requestId = request.itemId()) },
            true
        )
    }

    private fun updateSearch(request: WallpaperRequest) {
        if (request is WallpaperRequest.SEARCH) {
            val query = request.query()
            if (search != query) {
                request.initialPage()
                wallpapers.remove(request.itemId())
            }
            search = query

        }
    }



    override fun read(): Triple<GalleriesDomain, Int, WallpaperRequest> {
        val wallpaperCache = cache.read()
        val request = wallpaperCache.request()

        val id = request.itemId()

        val result = wallpapers.getOrElse(id) { WallpaperRequest.DATE() }.data()

       // val position = position(wallpaperCache.id(), result)

        val isEmpty = request.showProgress()
        return Triple(
            GalleriesDomain.Base(result.map { it.map(domainMapper) }, !isEmpty),
            wallpaperCache.id(),
            request
        )
    }

    override fun clear() = cache.removeLast()

    override fun clear(request: WallpaperRequest) {
        wallpapers.remove(request.itemId())
    }

    override fun clearAll() {
        wallpapers.clear()
    }

    override fun getCachedData(id: String): List<GalleryData> =
        wallpapers.getOrElse(id) { WallpaperRequest.DATE() }.data()


}