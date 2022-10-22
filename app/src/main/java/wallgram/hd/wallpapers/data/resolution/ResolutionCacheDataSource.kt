package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.core.Read
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionRepository

interface ResolutionCacheDataSource : ChangeResolution, IsSelected, CurrentResolution {

    class Base(
        private val resolutions: ResolutionsCache.Mutable
    ) : ResolutionCacheDataSource {

        private var cached = resolutions.read()

        override fun changeResolution(data: String) {
            resolutions.save(data)
            cached = resolutions.read()
        }

        override fun isSelected(id: String) = id == cached
        override fun currentResolution() = cached
    }
}