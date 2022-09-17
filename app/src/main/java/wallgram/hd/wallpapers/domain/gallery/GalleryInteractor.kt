package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import javax.inject.Inject

interface GalleryInteractor {

    suspend fun gallery(
        wallpaperRequest: WallpaperRequest,
        atFinish: () -> Unit,
        successful: (GalleriesUi) -> Unit
    )

    fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean

    fun save(wallpaperRequest: WallpaperRequest, position: Int)
    fun update(wallpaperRequest: WallpaperRequest, position: Int)
    fun read(): Triple<GalleriesUi, Int, WallpaperRequest>
    fun clear()

    class Base @Inject constructor(
        private val mapper: GalleriesDomain.Mapper<GalleriesUi>,
        private val repository: GalleryRepository,
        private val dispatchers: Dispatchers,
        private val handleError: HandleError
    ) : Interactor.Abstract(dispatchers, handleError), GalleryInteractor {

        override suspend fun gallery(
            wallpaperRequest: WallpaperRequest,
            atFinish: () -> Unit,
            successful: (GalleriesUi) -> Unit,
        ) {
            try {
                val result = repository.gallery(wallpaperRequest)
                val data = result.map(mapper)
                dispatchers.changeToUI { successful.invoke(data) }
            } catch (error: Exception) {
                val data = GalleriesUi.Base(listOf(GalleryUi.Error(error)))
                dispatchers.changeToUI { successful.invoke(data) }
                handleError.handle(error)
            }
            finally {
                dispatchers.changeToUI { atFinish.invoke() }
            }
        }

        override fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean =
            with(repository.getCachedData()) {
                return isNotEmpty() && size - 1 == lastVisibleItemPosition
            }

        override fun save(wallpaperRequest: WallpaperRequest, position: Int) =
            repository.save(wallpaperRequest, position)

        override fun update(wallpaperRequest: WallpaperRequest, position: Int) =
            repository.update(wallpaperRequest, position)

        override fun read(): Triple<GalleriesUi, Int, WallpaperRequest> {
            val cache = repository.read()
            val galleriesUi = cache.first.map(mapper)
            val position = cache.second
            val request = cache.third
            return Triple(galleriesUi, position, request)
        }

        override fun clear() = repository.clear()

    }
}