package wallgram.hd.wallpapers.domain.pic

import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.domain.Interactor
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.pic.PicUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoListUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoUi
import javax.inject.Inject

interface PicInteractor {

    suspend fun pic(
        id: Int,
        atFinish: () -> Unit,
        successful: (PicUi) -> Unit
    )

    suspend fun info(
        id: Int,
        atFinish: () -> Unit,
        successful: (InfoListUi) -> Unit
    )

    class Base @Inject constructor(
        private val mapper: PicDomain.Mapper<PicUi>,
        private val infoMapper: PicDomain.Mapper<InfoListUi>,
        private val repository: PicRepository,
        dispatchers: Dispatchers,
        handleError: HandleError
    ) : Interactor.Abstract(dispatchers, handleError), PicInteractor {

        override suspend fun pic(
            id: Int,
            atFinish: () -> Unit,
            successful: (PicUi) -> Unit,
        ) = handle(successful, atFinish) {
            val data = repository.pic(id)
            return@handle data.map(mapper)
        }

        override suspend fun info(
            id: Int,
            atFinish: () -> Unit,
            successful: (InfoListUi) -> Unit
        ) = handle(successful, atFinish) {
            val data = repository.pic(id)
            return@handle data.map(infoMapper)
        }
    }
}