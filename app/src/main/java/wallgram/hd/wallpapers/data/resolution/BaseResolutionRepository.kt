package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.domain.resolution.ResolutionRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionsDomain

class BaseResolutionRepository(
    private val dataSource: ResolutionsDataSource,
    private val cacheDataSource: ResolutionCacheDataSource,
    private val mapper: Resolution.Mapper<ResolutionsDomain>
) : ResolutionRepository {

    override fun resolutions(): ResolutionsDomain {
        val resolutions = dataSource.resolutions()
        return resolutions.map(mapper)
    }

    override fun currentResolution() = cacheDataSource.currentResolution()

}