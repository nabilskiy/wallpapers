package wallgram.hd.wallpapers.domain.favorites

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import javax.inject.Inject

interface FavoritesInteractor {

    suspend fun favorites(
        atFinish: () -> Unit,
        successful: (GalleriesUi) -> Unit
    )

    class Base @Inject constructor(
        private val mapper: GalleriesDomain.Mapper<GalleriesUi>,
        private val repository: GalleryRepository,
        dispatchers: Dispatchers,
        handleError: HandleError
    ) : Interactor.Abstract(dispatchers, handleError), FavoritesInteractor {

        override suspend fun favorites(
            atFinish: () -> Unit,
            successful: (GalleriesUi) -> Unit,
        ) = handle(successful, atFinish) {
            val data = repository.favorites()
            return@handle data.map(mapper)
        }

    }
}