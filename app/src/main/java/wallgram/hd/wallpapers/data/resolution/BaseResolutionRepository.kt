package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.domain.resolution.ResolutionRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionsDomain

class BaseResolutionRepository(
    private val dataSource: ResolutionsDataSource,
    private val cacheDataSource: ResolutionCacheDataSource,
    private val mapper: Resolution.Mapper<ResolutionsDomain>,
    private val displayProvider: DisplayProvider
) : ResolutionRepository {

    override fun resolutions(): ResolutionsDomain {
        val result = ArrayList<String>()

        val resolutions = dataSource.resolutions()
        val screen = screenResolution()

        result.addAll(resolutions)

        if (result.contains(screen)) {
            result.remove(screen)
        }

        result.add(0, screen)

        val data = Resolution.Base(result)

        return data.map(mapper)
    }

    override fun screenResolution() = displayProvider.getScreen()

    override fun currentResolution() = cacheDataSource.currentResolution()

}