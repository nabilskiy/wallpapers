package wallgram.hd.wallpapers.domain.resolution

import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionUi
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionsUi


interface ResolutionsInteractor {

    fun resolutions(): ResolutionsUi
    fun currentResolution(): String

    class Base(
        private val repository: ResolutionRepository,
        private val mapper: ResolutionsDomain.Mapper<ResolutionsUi>
    ) : ResolutionsInteractor {

        override fun resolutions(): ResolutionsUi {
            val data = repository.resolutions()
            return data.map(mapper)
        }

        override fun currentResolution() = repository.currentResolution()

    }
}