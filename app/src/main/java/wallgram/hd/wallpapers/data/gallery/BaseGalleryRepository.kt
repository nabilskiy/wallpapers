package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import javax.inject.Inject

class BaseGalleryRepository @Inject constructor(
    private val cloudDataSource: GalleryCloudDataSource,
    private val cache: WallpapersCache.Mutable,
    private val mapper: GalleryCloud.Mapper<GalleryData>,
    private val domainMapper: GalleryData.Mapper<GalleryDomain>
) : GalleryRepository {

    private var page = 1
    private var search = ""

    private val dataList = ArrayList<GalleryData>()

    override suspend fun gallery(wallpaperRequest: WallpaperRequest): GalleriesDomain {

        updateSearch(wallpaperRequest)

        val gallery = cloudDataSource.gallery(wallpaperRequest, page)
        val list = gallery.list.map { it.map(mapper) }
        if (list.isNotEmpty()) {
            dataList.addAll(list)
            page++
        }
        val isEmpty = list.isEmpty()
        return GalleriesDomain.Base(dataList.map { it.map(domainMapper) }, isEmpty)
    }

    private fun updateSearch(wallpaperRequest: WallpaperRequest) {
        if (wallpaperRequest is WallpaperRequest.SEARCH) {
            val query = wallpaperRequest.query()
            if (search != query) {
                dataList.clear()
                page = 1
            }
            search = query

        }
    }

//    override suspend fun search(query: String): GalleriesDomain {
//        val gallery = cloudDataSource.gallery(query, page)
//        val list = gallery.list
//        if (list.isNotEmpty()) {
//            dataList.addAll(list)
//            page++
//        }
//        return GalleriesDomain.Base(dataList.map { it.map(mapper) })
//    }

    override fun save(wallpaperRequest: WallpaperRequest, position: Int) {
        val wallpaperCache = WallpaperCache.Base(dataList, position, wallpaperRequest)
        cache.save(wallpaperCache)
    }

    override fun update(wallpaperRequest: WallpaperRequest, position: Int) {
        val wallpaperCache = WallpaperCache.Base(dataList, position, wallpaperRequest)
        cache.update(wallpaperCache)
    }

    override fun read(): Triple<GalleriesDomain, Int, WallpaperRequest> {
        val wallpaperCache = cache.read()
        val list = wallpaperCache.list()
        val position = wallpaperCache.position()

        dataList.clear()
        if (list.isNotEmpty()) {
            dataList.addAll(list)
            page = (position / 27) + 1
        }

        val isEmpty = wallpaperCache.wallpaperRequest().showProgress()
        val request = wallpaperCache.wallpaperRequest()
        return Triple(GalleriesDomain.Base(list.map { it.map(domainMapper) }, !isEmpty), position, request)
    }

    override fun clear() {
        cache.removeLast()
    }

    override fun getCachedData() = dataList


}