package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import javax.inject.Inject

interface GalleryInteractor {

    suspend fun gallery(
        wallpaperRequest: WallpaperRequest,
        atFinish: () -> Unit,
        successful: (GalleriesUi) -> Unit
    )

    suspend fun favorites(
        atFinish: () -> Unit,
        successful: (GalleriesUi) -> Unit
    )

//    suspend fun history(
//        atFinish: () -> Unit,
//        successful: (GalleriesUi) -> Unit
//    )

    fun needToLoadMoreData(id: String, lastVisibleItemPosition: Int): Boolean

    fun read(): Triple<GalleriesDomain, Int, WallpaperRequest>
    fun clear()
    fun clear(request: WallpaperRequest)

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
            } finally {
                dispatchers.changeToUI { atFinish.invoke() }
            }
        }

        override suspend fun favorites(
            atFinish: () -> Unit,
            successful: (GalleriesUi) -> Unit,
        ) = handle(successful, atFinish) {
            val data = repository.favorites()
            return@handle data.map(mapper)
        }


        override fun needToLoadMoreData(id: String, lastVisibleItemPosition: Int): Boolean =
            with(repository.getCachedData(id)) {
                return isNotEmpty() && size == lastVisibleItemPosition
            }

        private fun position(id: Int, data: GalleriesDomain) =
            data.map(GalleriesDomain.Mapper.Position(id))

        override fun read(): Triple<GalleriesDomain, Int, WallpaperRequest> {
            val cache = repository.read()
            val data = cache.first
            // val galleriesUi = cache.first.map(mapper)
            val request = cache.third

            return Triple(data, cache.second, request)
        }

        override fun clear() = repository.clear()

        override fun clear(request: WallpaperRequest) = repository.clear(request)

    }
}