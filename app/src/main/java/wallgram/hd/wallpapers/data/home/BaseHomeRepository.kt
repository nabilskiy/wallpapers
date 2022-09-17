package wallgram.hd.wallpapers.data.home

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.data.filters.FiltersCloudDataSource
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.data.gallery.WallpaperCache
import wallgram.hd.wallpapers.data.gallery.WallpapersCache
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.home.HomeDomain
import wallgram.hd.wallpapers.domain.home.HomeRepository
import wallgram.hd.wallpapers.domain.home.HomesDomain
import javax.inject.Inject

class BaseHomeRepository @Inject constructor(
    private val cloudDataSource: FiltersCloudDataSource,
    private val cache: WallpapersCache.Mutable,
    private val mapper: FiltersCloud.Mapper<HomeDomain>,
    private val domainMapper: GalleryData.Mapper<GalleryDomain>
) : HomeRepository {

    private val dataList = ArrayList<FiltersCloud>()

    override suspend fun filters(): HomesDomain {
        val categories = cloudDataSource.filters()

        if (categories.isNotEmpty()) {
            dataList.addAll(categories)
        }

        return HomesDomain.Base(categories.map { it.map(mapper) })
    }

    override fun save(wallpaperRequest: WallpaperRequest, position: Int, filter: Int) {
        dataList.find { it.map(FiltersCloud.Mapper.Id(filter)) }?.let { item ->
            val wallpaperCache = WallpaperCache.Base(
                item.map(FiltersCloud.Mapper.Test()),
                position,
                wallpaperRequest
            )
            cache.save(wallpaperCache)
        }
    }

    override fun read(): GalleriesDomain {
        val list = cache.read().list()
        return GalleriesDomain.Base(list.map { it.map(domainMapper) }, list.isEmpty())
    }

    override fun position() = cache.read().position()
}