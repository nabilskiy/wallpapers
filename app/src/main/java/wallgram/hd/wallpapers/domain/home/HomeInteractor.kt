package wallgram.hd.wallpapers.domain.home

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import javax.inject.Inject

interface HomeInteractor {

    suspend fun filters(
        atFinish: () -> Unit,
        successful: (FiltersUi) -> Unit
    )

    fun save(wallpaperRequest: WallpaperRequest, position: Int, filter: Int)
    fun read(): GalleriesUi

    class Base @Inject constructor(
        private val mapper: HomesDomain.Mapper<FiltersUi>,
        private val galleryMapper: GalleriesDomain.Mapper<GalleriesUi>,
        private val repository: HomeRepository,
        dispatchers: Dispatchers,
        handleError: HandleError
    ) : Interactor.Abstract(dispatchers, handleError), HomeInteractor {

        override suspend fun filters(
            atFinish: () -> Unit,
            successful: (FiltersUi) -> Unit
        ) = handle(successful, atFinish) {
            val data = repository.filters()
            return@handle data.map(mapper)
        }

        override fun save(wallpaperRequest: WallpaperRequest, position: Int, filter: Int) = repository.save(wallpaperRequest, position, filter)
        override fun read(): GalleriesUi = repository.read().map(galleryMapper)
    }
}